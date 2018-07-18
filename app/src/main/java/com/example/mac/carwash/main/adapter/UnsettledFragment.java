package com.example.mac.carwash.main.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mac.carwash.R;
import com.example.mac.carwash.main.order.OrderAdapter;
import com.example.mac.carwash.main.order.OrderBean;
import com.example.mac.carwash.util.JsonUtils;
import com.example.mac.carwash.view.RecycleViewDivider;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class UnsettledFragment extends Fragment {

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











	public List<OrderBean.Data> initOrderBean(){
		List<OrderBean.Data> dataList;
		String result = JsonUtils.getJson(getContext(), "member.json");
		Gson gson = new Gson();
		dataList = gson.fromJson(result, List.class);
		return dataList;
	}
}
