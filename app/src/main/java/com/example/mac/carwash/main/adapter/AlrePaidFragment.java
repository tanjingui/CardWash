package com.example.mac.carwash.main.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mac.carwash.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

public class AlrePaidFragment extends Fragment {

	private View view;
	private RecyclerView mRecyclerView;
	private RefreshLayout mRefreshLayout;
	Boolean flag = true; //判断是否初次加载
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.viewpaper_tab2, container,false);
		return view;
	}

}
