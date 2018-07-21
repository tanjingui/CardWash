package com.example.mac.carwash.main.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.constants.UserInfoState;
import com.example.mac.carwash.jsonBean.MemberWarshCarRecordsInfo;
import com.example.mac.carwash.main.order.MemberOrderAdapter;
import com.example.mac.carwash.view.RecycleViewDivider;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;
//import net.sf.json.JSONArray;

public class AllOrderFragment extends Fragment {

	private View view;
	private RecyclerView mRecyclerView;
	private RefreshLayout mRefreshLayout;
	private FragmentActivity mActivity;

//-------------------------刷新变量----------------------------------------
	MemberOrderAdapter mMemberOrderAdapter;
	Boolean flag = true;
	Boolean hasNextPage = true;
	int currentPage = 1;


	public static AllOrderFragment newInstance() {
		AllOrderFragment fragment = new AllOrderFragment();
		return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		mActivity = getActivity();
		view = inflater.inflate(R.layout.viewpaper_tab1, container,false);
		init();
		return view;
	}


	public void init(){
		mRecyclerView=(RecyclerView) view.findViewById(R.id.rv);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		mRecyclerView.addItemDecoration(new RecycleViewDivider(
								view.getContext(), LinearLayoutManager.VERTICAL, 12, getResources().getColor(R.color.black)));
		queryTodayAllMemberWarshCarRecords(-1);
		mRefreshLayout =(RefreshLayout) view.findViewById(R.id.refreshLayout);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		//刷新
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				currentPage = 1;
				queryTodayAllMemberWarshCarRecords(0);
			}
		});

		//加载更多
		 mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
		 @Override
		 public void onLoadmore(RefreshLayout refreshlayout) {
			 if(hasNextPage==false){	Toast.makeText(mActivity,"下拉没有更多数据了！",Toast.LENGTH_SHORT).show();mRefreshLayout.finishLoadmore();return; }
			 queryTodayAllMemberWarshCarRecords(1);
		 } });
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = true;
	}
	public void add(){
		currentPage++;
	}
	public void reset(){
		currentPage=1;
	}







	/*-----------------------------查询当天所有会员用户订单记录--------------------------------------------------*/
	private void queryTodayAllMemberWarshCarRecords(final int operation) {
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("sqlKey",  "CS_XICHE_LIST");
        resMap.put("store", UserInfoState.getSelectStoreCode());
		resMap.put("state",""); //传入需要查找订单类别   试了没用？？？？？？ //0是未结算 1是已结算 ""是全部的-------------
		resMap.put("sqlType", "sql");
		Map<String,Object>page = new HashMap<>();
		page.put("currentPage",currentPage);
		page.put("pageRecordCount",3); //数据库查询到的总数/7  然后选择要查看第几页
		resMap.put("page",page);
		WebServiceHelp mServiceHelp = new WebServiceHelp(mActivity,"iPadService.asmx", "loadDataList", PubData.class,false,"2");
		mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
			@Override
			public void onServiceCallBackString(boolean haveCallBack, String json) {
				Log.i("uuu服务器返回所有当天会员用户洗车信息：：",""+json);
				Gson gson = new Gson();
				MemberWarshCarRecordsInfo memberWarshCarRecordsInfo = gson.fromJson(json, MemberWarshCarRecordsInfo.class);
				if(memberWarshCarRecordsInfo.getCode().equals("01")){
					mMemberOrderAdapter.clearAll();Toast.makeText(mActivity,"没有数据！",Toast.LENGTH_SHORT).show(); if(operation==0){mRefreshLayout.finishRefresh();}else if(operation==1){mRefreshLayout.finishLoadmore();}  reset(); return;
				}else if(memberWarshCarRecordsInfo.getCode().equals("00")){
                     if(memberWarshCarRecordsInfo.getPage().getHasNextPage()==false){
						 hasNextPage = false;
					 }
					if(flag){ //初次加载
						mMemberOrderAdapter = new MemberOrderAdapter(memberWarshCarRecordsInfo.getData(),getContext());
						mRecyclerView.setAdapter(mMemberOrderAdapter);flag=false;reset();add();
					}else{
						if(operation==0){  //刷新肯定不是初次加载
							mMemberOrderAdapter.refresh(memberWarshCarRecordsInfo.getData());flag = false;mRefreshLayout.finishRefresh();reset();add();hasNextPage=true;
						}else if(operation==1){
							mMemberOrderAdapter.add(memberWarshCarRecordsInfo.getData());flag = false;mRefreshLayout.finishLoadmore();add();
						}
					}
				}else{
					Toast.makeText(mActivity,"未知错误！ code="+memberWarshCarRecordsInfo.getCode(),Toast.LENGTH_SHORT).show();return;
				}
				mRefreshLayout.finishRefresh();
				mRefreshLayout.finishLoadmore();
			}
		});
		mServiceHelp.start(resMap,mActivity);
	}

}
