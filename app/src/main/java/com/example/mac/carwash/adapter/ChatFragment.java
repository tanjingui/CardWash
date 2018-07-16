package com.example.mac.carwash.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mac.carwash.R;
import com.example.mac.carwash.order.OrderAdapter;
import com.example.mac.carwash.order.OrderBean;
import com.example.mac.carwash.view.RecycleViewDivider;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

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











	public  OrderBean initOrderBean(){
		OrderBean orderBean;
		List<OrderBean.Data>dataList;
			orderBean = new OrderBean();
		    dataList = new ArrayList<>();
			OrderBean.Data data1 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","普通体验会员","2017-08-15 02:30" +
					"","2018071102364");
			OrderBean.Data data2 = orderBean.new Data("http://c.hiphotos.baidu.com/image/pic/item/72f082025aafa40f99d4e82aa764034f78f01932.jpg","0","新asafd4","已结算","李万海","大众A级会员","2015-08-15 22:30","2018071102364");
			OrderBean.Data data3 = orderBean.new Data("http://d.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828b1d864115f11f4134960a5a9b.jpg","0","粤asfd54","已结算","安没差","普通体验会员","2014-08-15 02:30","2018071102364");
			OrderBean.Data data4 = orderBean.new Data("","0元","粤asfd54","已结算","万经思","普通体验会员","2015-8-15","2018071102364");
			OrderBean.Data data5 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","体验会员","2017-08-15 02:30","2018071102364");
			OrderBean.Data data6 = orderBean.new Data("","0元","粤asfd54","已结算","万经思","普通体验会员","2015-8-15","2018071102364");
			OrderBean.Data data7 = orderBean.new Data("http://g.hiphotos.baidu.com/image/pic/item/2cf5e0fe9925bc319a8efc6352df8db1ca13707e.jpg","0","京asfd54","已结算","万经思","普通体验会员","2017-08-15 02:30","2018071102364");
			OrderBean.Data data8 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","普通体验会员","2017-08-15 02:30","2018071102364");
			OrderBean.Data data9 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","非asfd54","已结算","万经思","普通体验会员","2017-08-15 02:30","2018071102364");
			OrderBean.Data data10 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","体验会员","2017-08-15 02:30","2018071102364");
			OrderBean.Data data11 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","体验会员","2017-08-15 02:30","2018071102364");
			OrderBean.Data data12 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","体验会员","2017-08-15 02:30","2018071102364");
			OrderBean.Data data13 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","体验会员","2015-8-15","2018071102364");
			OrderBean.Data data14 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","体验会员","2015-8-15","2018071102364");
			OrderBean.Data data15 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","体验会员","2015-8-15","2018071102364");
			OrderBean.Data data16 = orderBean.new Data("https://b-ssl.duitang.com/uploads/item/201605/05/20160505145557_dtYHf.jpeg","0","粤asfd54","已结算","万经思","体验会员","2015-8-15","2018071102364");
		    dataList.add(data1);dataList.add(data2);dataList.add(data3);dataList.add(data4);dataList.add(data5);dataList.add(data6);dataList.add(data7);dataList.add(data8);dataList.add(data9);dataList.add(data10);dataList.add(data11);dataList.add(data12);dataList.add(data13);dataList.add(data14);dataList.add(data15);dataList.add(data16);
		    orderBean.setData(dataList);
		return orderBean;
	}
}
