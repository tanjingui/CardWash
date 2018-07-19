package com.example.mac.carwash.main.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.temp_tab_chat, container,false);
		init();
		return view;
	}



	public void init(){
		mRecyclerView=(RecyclerView) view.findViewById(R.id.rv);
		//这一句不加就得die！！！！！！！！！研究一下
		mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		queryTodayAllMemberWarshCarRecords();
		mRefreshLayout =(RefreshLayout) view.findViewById(R.id.refreshLayout);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		//刷新
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
//				mData.clear();
//				mNameAdapter.notifyDataSetChanged();
				queryTodayAllMemberWarshCarRecords();
			}
		});

		//加载更多
		 mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
		 @Override
		 public void onLoadmore(RefreshLayout refreshlayout) {
//		 for(int i=0;i<30;i++){ mData.add("小明"+i); }
//		 mNameAdapter.notifyDataSetChanged();
		 refreshlayout.finishLoadmore();
		 } });
	}

//	public List<Data> initOrderBean(){
//		Gson gson = new Gson();
//		OrderInfoBean orderInfoBean;
//		List<Data> dataList;
//     	String result = JsonUtils.getJson(getContext(), "member.json");
//		orderInfoBean = gson.fromJson(result, OrderInfoBean.class);
//		dataList = orderInfoBean.getData();
//		return dataList;
//	}

	Boolean flag = true;
	private void queryTodayAllMemberWarshCarRecords() {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("sessionId", "6a874b1f630b4785a83f30052952e17e");
		resMap.put("sqlKey",  "CS_XICHE_LIST");
		resMap.put("sqlType", "sql");
		Map<String,Object>page = new HashMap<>();
		page.put("currentPage",1);
		page.put("pageRecordCount",1);
		resMap.put("page",page);
		WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadDataList", PubData.class,flag,"2");
		mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
			@Override
			public void onServiceCallBackString(boolean haveCallBack, String json) {
				Log.i("uuu服务器返回所有当天会员用户洗车信息：：",""+json);
				Gson gson = new Gson();
				MemberWarshCarRecordsInfo memberWarshCarRecordsInfo = gson.fromJson(json, MemberWarshCarRecordsInfo.class);
				//将server传来的数据 适配recyclerView
				Log.i("解析出来的信息：：",""+memberWarshCarRecordsInfo.toString());
				MemberOrderAdapter adapter = new MemberOrderAdapter(memberWarshCarRecordsInfo.getData(),getContext());
				mRecyclerView.setAdapter(adapter);
				mRecyclerView.addItemDecoration(new RecycleViewDivider(
						view.getContext(), LinearLayoutManager.VERTICAL, 12, getResources().getColor(R.color.black)));
				if(flag==false){  //服务器已经完成数据更新 可以取消上拉刷新等待动画
					mRefreshLayout.finishRefresh();
				}
				flag = false;

			}
		});
		mServiceHelp.start(resMap,getActivity());
	}

}
