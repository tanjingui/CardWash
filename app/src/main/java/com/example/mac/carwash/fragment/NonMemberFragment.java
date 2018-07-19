package com.example.mac.carwash.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.activity.BaseActivity;
import com.example.mac.carwash.jsonBean.OpenBillInfoBean;
import com.example.mac.carwash.main.order.OrderAdapter;
import com.example.mac.carwash.jsonBean.OrderInfoBean;
import com.example.mac.carwash.util.JsonUtils;
import com.example.mac.carwash.util.LicenseKeyboardUtil;
import com.example.mac.carwash.util.ScreenSizeUtils;
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

/**
 * Created by mac on 2018/7/9.
 */

public class NonMemberFragment extends Fragment implements View.OnClickListener{

    private View view;
    public static final String INPUT_LICENSE_COMPLETE = "com.example.mac.carwash.order.input.comp";
    public static final String INPUT_LICENSE_KEY = "LICENSE";
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private LinearLayout linearLayout;
    private EditText inputbox1,inputbox2,
            inputbox3,inputbox4,
            inputbox5,inputbox6,inputbox7;
    private LinearLayout boxesContainer;
    private LicenseKeyboardUtil keyboardUtil;
    private KeyboardView keyboardView;
    private  BaseActivity activity;
    private IntentFilter finishFilter;
    //判断广播是否被注册
    private boolean mReceiverTag = false;
    public static NonMemberFragment newInstance() {
        NonMemberFragment fragment = new NonMemberFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.layout_interface_nonmember, container,false);
        init();
        return view;
    }

    public void init(){
        activity=(BaseActivity) getActivity();
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_unMember_info);
        mRefreshLayout =(RefreshLayout) view.findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_unMember_info);
        OrderAdapter adapter = new OrderAdapter(initOrderBean(),getContext());
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
        inputbox1 = (EditText) view.findViewById(R.id.et_car_license_inputbox1);
        inputbox2 = (EditText) view.findViewById(R.id.et_car_license_inputbox2);
        inputbox3 = (EditText) view.findViewById(R.id.et_car_license_inputbox3);
        inputbox4 = (EditText) view.findViewById(R.id.et_car_license_inputbox4);
        inputbox5 = (EditText) view.findViewById(R.id.et_car_license_inputbox5);
        inputbox6 = (EditText) view.findViewById(R.id.et_car_license_inputbox6);
        inputbox7 = (EditText) view.findViewById(R.id.et_car_license_inputbox7);
        ImageView btn_close = (ImageView) view.findViewById(R.id.btn_close_input);
        Button button = (Button) view.findViewById(R.id.btn_add_car);
        Button btn_read_unmember_info = (Button)view.findViewById(R.id.btn_read_unmember_info);
        button.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        btn_read_unmember_info.setOnClickListener(this);
        mRecyclerView.setOnClickListener(this);
        boxesContainer = (LinearLayout) view.findViewById(R.id.ll_car_license_inputbox_content);
        keyboardView = (KeyboardView)view.findViewById(R.id.keyboard_view);
        //输入车牌完成后的intent过滤器
        finishFilter = new IntentFilter(INPUT_LICENSE_COMPLETE);
        activity.registerReceiver(receiver, finishFilter);
        mReceiverTag = true;
    }

    //当键盘输入完毕时候，activity接收到键盘输入的所有内容，则此次广播结束
    //所以每次出来键盘时，要重新注册广播
    final BroadcastReceiver receiver =  new  BroadcastReceiver() {
        @Override
        public   void  onReceive(Context context, Intent intent) {
            String license = intent.getStringExtra(INPUT_LICENSE_KEY);
            if(license != null && license.length() > 0){
                boxesContainer.setVisibility(View.GONE);
                if(keyboardUtil != null){
                    keyboardUtil.hideKeyboard();
                }
                customDialog(license,50);
            }
            activity.unregisterReceiver(this);
            mReceiverTag = false;
        }
    };


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_add_car:
                if(!mReceiverTag){
                activity.registerReceiver(receiver, finishFilter);mReceiverTag=true;}
                linearLayout.setVisibility(View.INVISIBLE);
                //mRecyclerView.setVisibility(View.INVISIBLE);
                boxesContainer.setVisibility(View.VISIBLE);
                keyboardUtil = new LicenseKeyboardUtil(getContext(),new EditText[]{inputbox1,inputbox2,inputbox3,
                        inputbox4,inputbox5,inputbox6,inputbox7});
                keyboardUtil.showKeyboard();
                break;
            case R.id.btn_read_unmember_info:
                if(mReceiverTag){
                activity.unregisterReceiver(receiver);mReceiverTag=false;}
                boxesContainer.setVisibility(View.GONE);
                keyboardView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
              //  mRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_close_input:
                if(mReceiverTag){
                    activity.unregisterReceiver(receiver);mReceiverTag=false;}
                boxesContainer.setVisibility(View.GONE);
                keyboardView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    private void customDialog(final String carNum, int price) {
        String title =String.format("车牌号为 "+"<font color=#FF0000 size=20>%s</font>" +"，金额为"+"<font color=#FF0000 size=20>%s</font>元，"+ "\n确认提交？", carNum,price);
        final Dialog dialog = new Dialog(getActivity(), R.style.NormalDialogStyle);
        View view = View.inflate(getActivity(), R.layout.dialog_normal, null);
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        dialog_content.setText(Html.fromHtml(title));
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(getActivity()).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(getActivity()).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardUtil = null;
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBillByCarNumRequest(carNum.substring(0,1),carNum.substring(1,carNum.length()));
               // Toast.makeText(activity,"提交成功",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!mReceiverTag){
            activity.unregisterReceiver(receiver);
            mReceiverTag=false;
        }
    }



    //
    private void openBillByCarNumRequest(String province,String carmark) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sessionId", "d86bd28a13a248cf9cf23ac04dfd2818");
        resMap.put("province",  province);
        resMap.put("sqlKey", "CP_ADD_XICHE_ORDERV2");
        resMap.put("carmark", carmark);
        resMap.put("sqlType", "proc");
        WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadData", PubData.class,true,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                Log.i("uuu非会员录入车牌信息洗车开单回调：",""+json);
				Gson gson = new Gson();
				OpenBillInfoBean openBillInfoBean = gson.fromJson(json, OpenBillInfoBean.class);
				OpenBillInfoBean.Data data = openBillInfoBean.getData();
				if( data.getOCODE().equals("00")){
					Toast.makeText(getActivity(),"洗车开单成功!",Toast.LENGTH_SHORT).show();
				}else if(data.getOCODE().equals("99")){
					Toast.makeText(getActivity(),"失败了，存在未结算的洗车单",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(),"未知错误",Toast.LENGTH_SHORT).show();
				}
            }
        });
        mServiceHelp.start(resMap,getActivity());
    }
}
