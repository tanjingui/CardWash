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

import com.example.mac.carwash.R;
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
		queryTodayAllMemberWarshCarRecords(currentPage,-1);
		mRefreshLayout =(RefreshLayout) view.findViewById(R.id.refreshLayout);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		//刷新
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				queryTodayAllMemberWarshCarRecords(currentPage,0);
			}
		});

		//加载更多
		 mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
		 @Override
		 public void onLoadmore(RefreshLayout refreshlayout) {
			 queryTodayAllMemberWarshCarRecords(currentPage,1);
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















	/*-----------------------------查询当天所有会员用户订单记录--------------------------------------------------*/
/*
*  #param operation 0是执行下拉刷新，1是执行加载更多 传入-1为初次加载
*  currentPage 当面请求页面
* */
	Boolean flag = true;//判断是否第一次进入，第一次进入需要走的流程全写出来
	Boolean hasNext = false;  //是否有下一页也有进行判断
	int currentPage = 1;
	final int pageRecordCount = 7; //默认一页显示7条
	//先解决下拉刷新，传入1，不用动flag标志，直接调用通用的mAdapter.refresh(getData()),flag==false中的停止刷新
	//再解决上拉加载更多，传入i++，不能refresh，
	//最后解决是否滑到底 是否还有数据  进行判断  避免出错
	//可能的error：进来没有数据盲目的刷新和加载更多；刷到底没有数据，还继续刷
	MemberOrderAdapter mMemberOrderAdapter;
	private void queryTodayAllMemberWarshCarRecords(final int currentPage, final int operation) {
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("sqlKey",  "CS_XICHE_LIST");
        resMap.put("store","0001");
		//resMap.put("state","1"); //传入需要查找订单类别   试了没用？？？？？？ //0是未结算 1是已结算 ""是全部的-------------
		resMap.put("sqlType", "sql");
		Map<String,Object>page = new HashMap<>();
		page.put("currentPage",currentPage);  //
		page.put("pageRecordCount",pageRecordCount); //数据库查询到的总数/7  然后选择要查看第几页
		resMap.put("page",page);
		WebServiceHelp mServiceHelp = new WebServiceHelp(mActivity,"iPadService.asmx", "loadDataList", PubData.class,flag,"2");
		mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
			@Override
			public void onServiceCallBackString(boolean haveCallBack, String json) {
				Log.i("uuu服务器返回所有当天会员用户洗车信息：：",""+json);
				Gson gson = new Gson();
				MemberWarshCarRecordsInfo memberWarshCarRecordsInfo = gson.fromJson(json, MemberWarshCarRecordsInfo.class);
				//将server传来的数据 适配recyclerView
				Log.i("解析出来的信息：：",""+memberWarshCarRecordsInfo.toString());
				if(flag==true){
					//第一次进入 走默认的流程
					mMemberOrderAdapter = new MemberOrderAdapter(memberWarshCarRecordsInfo.getData(),getContext());
					mRecyclerView.setAdapter(mMemberOrderAdapter);
					mRecyclerView.addItemDecoration(new RecycleViewDivider(
							view.getContext(), LinearLayoutManager.VERTICAL, 12, getResources().getColor(R.color.black)));}
				else{
					//这里处理刷新 或者 加载更多的操作  需要对这个进行判断
					if(operation==0){mMemberOrderAdapter.refresh(memberWarshCarRecordsInfo.getData());mRefreshLayout.finishRefresh();}else if(operation==1){
						mMemberOrderAdapter.add(memberWarshCarRecordsInfo.getData());	mRefreshLayout.finishLoadmore();add();}
				}
				flag = false;
			}
		});
		mServiceHelp.start(resMap,mActivity);
	}
}
