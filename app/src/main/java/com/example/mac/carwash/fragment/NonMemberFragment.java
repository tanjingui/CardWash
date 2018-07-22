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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.activity.BaseActivity;
import com.example.mac.carwash.constants.UserInfoState;
import com.example.mac.carwash.jsonBean.OpenBillInfoBean;
import com.example.mac.carwash.jsonBean.PayResponseBean;
import com.example.mac.carwash.jsonBean.UnmemberWarshCarRecordsInfo;
import com.example.mac.carwash.main.order.UnMemberOrderAdapter;
import com.example.mac.carwash.util.LicenseKeyboardUtil;
import com.example.mac.carwash.util.ScreenSizeUtils;
import com.example.mac.carwash.view.RecycleViewDivider;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 2018/7/9.
 */

public class NonMemberFragment extends Fragment implements View.OnClickListener{

    private View view;
    public static final String INPUT_LICENSE_COMPLETE = "com.example.mac.carwash.unmember.order";
    public static final String INPUT_LICENSE_KEY = "LICENSE";
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private LinearLayout linearLayout;
    private Spinner spinner;
    private EditText inputbox1,inputbox2,
            inputbox3,inputbox4,
            inputbox5,inputbox6,inputbox7;
    private LinearLayout boxesContainer;
    private LicenseKeyboardUtil keyboardUtil;
    private KeyboardView keyboardView;
    private  BaseActivity mActivity;
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

        spinner = (Spinner) view.findViewById(R.id.toolbar_spinner_select_store);
        mActivity=(BaseActivity) getActivity();
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_unMember_info);
        mRefreshLayout =(RefreshLayout) view.findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_unMember_info);
        //初始化recyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRefreshLayout =(RefreshLayout) view.findViewById(R.id.refreshLayout);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                queryTodayNonMemberWarshCarRecords();

            }
        });

        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
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
//        mRecyclerView.setOnClickListener(this);
        boxesContainer = (LinearLayout) view.findViewById(R.id.ll_car_license_inputbox_content);
        keyboardView = (KeyboardView)view.findViewById(R.id.keyboard_view);
        //输入车牌完成后的intent过滤器
        finishFilter = new IntentFilter(INPUT_LICENSE_COMPLETE);
        mActivity.registerReceiver(receiver, finishFilter);   mReceiverTag = true;//广播被注册
        initToolBarSpinner();
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
            mActivity.unregisterReceiver(this);
            mReceiverTag = false;
        }
    };


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_add_car:
                if(!mReceiverTag){
                mActivity.registerReceiver(receiver, finishFilter);mReceiverTag=true;}
                linearLayout.setVisibility(View.INVISIBLE);
                //mRecyclerView.setVisibility(View.INVISIBLE);
                boxesContainer.setVisibility(View.VISIBLE);
                keyboardUtil = new LicenseKeyboardUtil(getContext(),new EditText[]{inputbox1,inputbox2,inputbox3,
                        inputbox4,inputbox5,inputbox6,inputbox7});
                keyboardUtil.showKeyboard();
                break;
            case R.id.btn_read_unmember_info:
                queryTodayNonMemberWarshCarRecords();
                if(mReceiverTag){
                mActivity.unregisterReceiver(receiver);mReceiverTag=false;}
                boxesContainer.setVisibility(View.GONE);
                keyboardView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
              //  mRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_close_input:
                if(mReceiverTag){
                    mActivity.unregisterReceiver(receiver);mReceiverTag=false;}
                boxesContainer.setVisibility(View.GONE);
                keyboardView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    private void customDialog(final String carNum, int price) {
        String title =String.format("车牌号为 "+"<font color=#FF0000 size=20>%s</font>" +"，金额为"+"<font color=#FF0000 size=20>%s</font>元，"+ "\n确认提交？", carNum,price);
        final Dialog dialog = new Dialog(mActivity, R.style.NormalDialogStyle);
        View view = View.inflate(mActivity, R.layout.dialog_normal2, null);
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        dialog_content.setText(Html.fromHtml(title));
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(mActivity).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(mActivity).getScreenWidth() * 0.75f);
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
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    private void customDialog2(final String carNum, int price) {
        String title =String.format("车牌号为 "+"<font color=#FF0000 size=20>%s</font>" +"，金额为"+"<font color=#FF0000 size=20>%s</font>元，"+ "\n请选择开单方式: ", carNum,price);
        final Dialog dialog = new Dialog(getActivity(), R.style.NormalDialogStyle);
        View view = View.inflate(getActivity(), R.layout.dialog_normal3, null);
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        TextView tv1 = (TextView) view.findViewById(R.id.btn_select1);
        TextView tv2 = (TextView) view.findViewById(R.id.btn_select2);
        TextView tv3 = (TextView) view.findViewById(R.id.btn_select3);
        tv1.setText("洗车卡结算"); tv2.setText("现金结算"); tv3.setText("新办会员");
        dialog_content.setText(Html.fromHtml(title));
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(getActivity()).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(getActivity()).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        //订单的价格变动需要谨慎处理  需考虑到各种突发情况
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //洗车卡结算
                //openPayForMemberRequest(1);
                dialog.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //现金结算
                //openPayForMemberRequest(2);
                dialog.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新办会员结算
                //openPayForMemberRequest(3);
                dialog.dismiss();
            }
        });
        dialog.show();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(!mReceiverTag){
            mActivity.unregisterReceiver(receiver);
            mReceiverTag=false;
        }
    }






    /*-----------------------------------------查询非会员订单请求--------------------------------------------------*/;
    private void queryTodayNonMemberWarshCarRecords() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sqlKey",  "CS_XICHE_LISTV2");
        resMap.put("sqlType", "sql");
        WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadDataList", PubData.class,false,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                Gson gson = new Gson();
                UnmemberWarshCarRecordsInfo unmemberWarshCarRecordsInfo = gson.fromJson(json, UnmemberWarshCarRecordsInfo.class);
                UnMemberOrderAdapter adapter = new UnMemberOrderAdapter(unmemberWarshCarRecordsInfo.getData(),getContext());
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.addItemDecoration(new RecycleViewDivider(
                        view.getContext(), LinearLayoutManager.VERTICAL, 12, getResources().getColor(R.color.black)));
                mRefreshLayout.finishRefresh();
            }
        });
        mServiceHelp.start(resMap,getActivity());
    }



    /*-----------------------------------------非会员开单请求--------------------------------------------------*/
    private void openBillByCarNumRequest(String province,String carmark) {
        Map<String, Object> resMap = new HashMap<String, Object>();
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



    /*-----------------------------------------结算请求--------------------------------------------------*/
    private void openPayForMemberRequest(int OrderId,int PayWay) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sqlKey", "CP_SETTLEMENT_XICHE_ORDER");
        resMap.put("settlementWay", PayWay);
        resMap.put("bcid",OrderId);
        resMap.put("sqlType", "proc");
        WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadData", PubData.class,true,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                Gson gson = new Gson();
                PayResponseBean payResponseBean = gson.fromJson(json, PayResponseBean.class);
                String code = payResponseBean.getCode();
                if (code.equals("00")) {
                    Toast.makeText(getActivity(), "结算成功！", Toast.LENGTH_SHORT).show();
                } else if(code.equals("99")){
                    Toast.makeText(getActivity(), "重复结算！", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "结算失败，未知错误！ code"+code, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        mServiceHelp.start(resMap,getActivity());
    }










    public void initToolBarSpinner(){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, new ArrayList<String>(UserInfoState.getStoreMap().keySet()));
        adapter.setDropDownViewResource(android.
                R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(UserInfoState.getSelectStoreIndex());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //记录当前选择的门店，和门店对应的code
                UserInfoState.setSelectStoreIndex(position);
                UserInfoState.setSelectStoreCode(new ArrayList<String>(UserInfoState.getStoreMap().values()).get(position));
                //设置显示当前选择的项
                parent.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
