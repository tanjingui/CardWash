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
import com.example.mac.carwash.jsonBean.OrderInfoBean;
import com.example.mac.carwash.main.order.OrderAdapter;
import com.example.mac.carwash.util.JsonUtils;
import com.example.mac.carwash.view.RecycleViewDivider;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlrePaidFragment extends Fragment {

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
		queryTodayAllWarshCarRecords();
		OrderAdapter adapter = new OrderAdapter(initOrderBean(),getContext());
		mRecyclerView=(RecyclerView) view.findViewById(R.id.rv);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.addItemDecoration(new RecycleViewDivider(
				view.getContext(), LinearLayoutManager.VERTICAL, 25, getResources().getColor(R.color.black)));
		mRefreshLayout =(RefreshLayout) view.findViewById(R.id.refreshLayout);
		//刷新
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
//				mData.clear();
//				mNameAdapter.notifyDataSetChanged();
				refreshlayout.finishRefresh();
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











	public List<OrderInfoBean.Data> initOrderBean(){
		Gson gson = new Gson();
		OrderInfoBean orderInfoBean;
		List<OrderInfoBean.Data> dataList;
		String result = JsonUtils.getJson(getContext(), "member.json");
		orderInfoBean = gson.fromJson(result, OrderInfoBean.class);
		dataList = orderInfoBean.getData();
		return dataList;
	}


    private void queryTodayAllWarshCarRecords() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sessionId", "6a874b1f630b4785a83f30052952e17e");
        resMap.put("sqlKey",  "CS_XICHE_LISTV2");
        resMap.put("sqlType", "sql");
        WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadDataList", PubData.class,true,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                Log.i("uuu服务器返回所有当天用户洗车部分信息：：",""+json);
            }
        });
        mServiceHelp.start(resMap,getActivity());
    }
}
