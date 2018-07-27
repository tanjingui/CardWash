package com.example.mac.carwash.main.record;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.example.mac.carwash.R;
import com.example.mac.carwash.jsonBean.MemberWarshCarRecordsBean;
import com.example.mac.carwash.view.RecycleViewDivider;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 2018/7/21.
 */
//-----------------查询当天特定会员用户订单记录-------------------
public class MemberWashCarRecordsFragment extends Fragment{
    private View view;
    RecyclerView rv_washRecords;
    Button btn_back;
    private String customerId;
    private String qrCode;
    private MemberRecordsAdapter mWashCarMemberRecordsAdapter;
    private BottomNavigationBar mBottomNavigationBar;
    //-------数据加载时动画--------
    AnimationDrawable mAnimation;
    ImageView mImageView;
    LinearLayout loadingLayout;
    public static MemberWashCarRecordsFragment newInstance(String settlementId,String qrCode) {
        Bundle args = new Bundle();
        args.putString("customerId",settlementId);
        args.putString("qrCode",qrCode);
        MemberWashCarRecordsFragment fragment = new MemberWashCarRecordsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_warshcard_records, null);
        init();
       queryMemberHistoryWarshCarRecords();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBottomNavigationBar.setVisibility(View.VISIBLE);
    }

    public void init(){
        startLoadingAnim();
        Bundle args = getArguments();
        customerId = args.getString("customerId");
        qrCode = args.getString("qrCode");
        mBottomNavigationBar = (BottomNavigationBar) getActivity().findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setVisibility(View.GONE);
        rv_washRecords = (RecyclerView) view.findViewById(R.id.washRecords_recycler_view);
        rv_washRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_washRecords.addItemDecoration(new RecycleViewDivider(
                view.getContext(), LinearLayoutManager.VERTICAL, 12, getResources().getColor(R.color.black)));
        btn_back = (Button)view.findViewById(R.id.toolbar_left_btn);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                mBottomNavigationBar.setVisibility(View.VISIBLE);
            }
        });
    }




    //-------启动动画-------
    public void startLoadingAnim(){
        mImageView = (ImageView) view.findViewById(R.id.loadingIv);
        loadingLayout = (LinearLayout)view.findViewById(R.id.anim_loading);
        mImageView.setBackgroundResource(R.drawable.frame_tyre);
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
    }
    //-------结束动画-------
    public void stopLoadingAnim(){
        mAnimation.stop();
        loadingLayout.setVisibility(View.GONE);
    }






    	/*-----------------------------查询当天特定会员用户订单记录--------------------------------------------------*/
        private void queryMemberHistoryWarshCarRecords() {
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("SETTLEMENTID",  customerId);
            resMap.put("sqlKey", "CS_xiche_history");
           resMap.put("qrcode", qrCode);
            resMap.put("sqlType", "sql");
            WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadDataList", PubData.class,false,"2");
            mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
                @Override
                public void onServiceCallBackString(boolean haveCallBack, String json) {
                    stopLoadingAnim();
                    Gson gson = new Gson();
                    MemberWarshCarRecordsBean memberWarshCarRecordsBean = gson.fromJson(json, MemberWarshCarRecordsBean.class);
                    if(memberWarshCarRecordsBean.getCode().equals("01")){
                        Toast.makeText(getActivity(),"没有数据！",Toast.LENGTH_SHORT).show();
                    }else if(memberWarshCarRecordsBean.getCode().equals("00")) {
                        mWashCarMemberRecordsAdapter = new MemberRecordsAdapter(memberWarshCarRecordsBean.getData(), getContext());
                        rv_washRecords.setAdapter(mWashCarMemberRecordsAdapter);
                    }else{
                        Toast.makeText(getActivity(),"未知错误！！ code="+memberWarshCarRecordsBean.getCode(),Toast.LENGTH_SHORT).show();return;
                    }
                }
            });
            mServiceHelp.start(resMap,getActivity());
        }





}
