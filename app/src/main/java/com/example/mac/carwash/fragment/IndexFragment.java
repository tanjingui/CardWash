package com.example.mac.carwash.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.activity.BaseActivity;
import com.example.mac.carwash.activity.CaptureActivity;
import com.example.mac.carwash.constants.UserInfoState;
import com.example.mac.carwash.jsonBean.CustomerInfoBean;
import com.example.mac.carwash.jsonBean.OpenBillInfoBean;
import com.example.mac.carwash.jsonBean.PayResponseBean;
import com.example.mac.carwash.main.record.MemberWashCarRecordsFragment;
import com.example.mac.carwash.util.Constant;
import com.example.mac.carwash.util.ScreenSizeUtils;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 2018/7/9.
 */

public class IndexFragment extends Fragment  {
    private View view;
    private Button btnQrCode;
    private Button btnWashCar;
    private Button btnWashCarRecords;
    private Button btnPay;
    private BaseActivity activity;
    private Spinner spinner;
    private TextView mOrderPrice;
    private CustomerInfoBean customerInfoBean=null;    //临时变量，保存扫码客户信息 非常重要
    private String scanResult="";
    //父activity fragmentManager引用
    private FragmentManager mFragmentManager;
    private FragmentTransaction transaction;

    public static IndexFragment newInstance() {
        IndexFragment fragment = new IndexFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_interface_index,container, false);
        activity=(BaseActivity) getActivity();
        initView();
        return view;
    }

    public void initView(){
        btnQrCode = (Button)view.findViewById(R.id.btn_QRCode);
        btnPay = (Button)view.findViewById(R.id.btn_pay);
        mOrderPrice = (TextView)view.findViewById(R.id.tv_info_order_price);
        spinner = (Spinner) view.findViewById(R.id.toolbar_spinner_select_store);
        initToolBarSpinner();

        btnWashCarRecords = (Button)view.findViewById(R.id.toolbar_right_btn);
        btnWashCar = (Button) view.findViewById(R.id.btn_washCard);
        view.findViewById(R.id.content);
        btnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
            }
        });
        btnWashCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerInfoBean==null){
                    Toast.makeText(getActivity(),"请先扫码！！",Toast.LENGTH_SHORT).show();
                    return;
                }
                customDialog1(mOrderPrice.getText().toString());
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerInfoBean==null){
                    Toast.makeText(getActivity(),"请先扫码！！",Toast.LENGTH_SHORT).show();
                    return;
                }customDialog2(mOrderPrice.getText().toString());
            }
        });
        btnWashCarRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerInfoBean==null){
                    Toast.makeText(getActivity(),"请先扫码！！",Toast.LENGTH_SHORT).show();
                    return;
                }addFragment(MemberWashCarRecordsFragment.newInstance(customerInfoBean.getData().getId()+"",scanResult),"records");
            }
        });
    }

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
             //这个bug还得继续测试------------------------------------------------------------
            //将扫描出的信息显示出来
            getCustomerInfoByQRCodeRequest(scanResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void customDialog1(String price) {
        String title =String.format("订单金额"+"<font color=#FF0000 size=20>%s</font>" + "元，确认开单？", price);
        final Dialog dialog = new Dialog(getActivity(), R.style.NormalDialogStyle);
        View view = View.inflate(getActivity(), R.layout.dialog_normal2, null);
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //订单的价格需要谨慎处理  需考虑到各种突发情况
                openBillForMemberRequest();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void customDialog2(String price) {
        String title =String.format("订单金额"+"<font color=#FF0000 size=20>%s</font>" + "元，确认结算？", price);
        final Dialog dialog = new Dialog(getActivity(), R.style.NormalDialogStyle);
        View view = View.inflate(getActivity(), R.layout.dialog_normal3, null);
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        TextView tv1 = (TextView) view.findViewById(R.id.btn_select1);
        TextView tv2 = (TextView) view.findViewById(R.id.btn_select2);
        TextView tv3 = (TextView) view.findViewById(R.id.btn_select3);
        tv1.setText("会员卡结算"); tv2.setText("洗车卡结算"); tv3.setText("现金结算");
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
                //会员卡结算
                openPayForMemberRequest(0);
                dialog.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //洗车卡结算
                openPayForMemberRequest(1);
                dialog.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //现金结算
                openPayForMemberRequest(2);
                dialog.dismiss();
            }
        });
        dialog.show();
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

    public void changeButtonState(){
        Button button =  (Button)view.findViewById(R.id.btn_washCard);
        button.setText("已开单");
        button.setTextColor(getResources().getColor(R.color.darkslategray));
        button.setEnabled(false);
    }

    private void replaceFragment(Fragment fragment) {
        mFragmentManager =getActivity().getSupportFragmentManager();
        transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addFragment(Fragment fragment,String tag){
        mFragmentManager = getActivity().getSupportFragmentManager();
        transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.content,fragment,tag);
        transaction.addToBackStack(null);
        transaction.commit();
        transaction.hide(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!isHidden()){
            transaction.show(this);
        }
    }

    //检查是否有id信息
    public Boolean checkState() {
        if (customerInfoBean.getData().getId() == 0) {
            Toast.makeText(getActivity(),"没获取到顾客id，先扫码获取！",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }



















     /*-----------------------------------------传入qrcode，请求顾客信息--------------------------------------------------*/
    private void getCustomerInfoByQRCodeRequest(String qrcode) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("qrcode", qrcode);
        resMap.put("sqlKey", "CS_xiche_info_by_qrcode");
        resMap.put("sqlType", "sql");
        WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadData", PubData.class,false,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                //这里需判断是否是有效的二维码，如果server回应不能识别，应拒return，避免出错
                Gson gson = new Gson();
                Log.i("999999999999",""+json+"");
                customerInfoBean = gson.fromJson(json, CustomerInfoBean.class);
                if(customerInfoBean.getCode().equals("00")){
                CustomerInfoBean.Data data = customerInfoBean.getData();
                Message message = new Message();
                message.what=111;message.obj = data;
                BaseActivity baseActivity = (BaseActivity) getActivity();
                baseActivity.handler.sendMessage(message);}
                else{
                    Toast.makeText(getActivity(),"存在异常 code="+customerInfoBean.getCode()+"",Toast.LENGTH_SHORT).show();return;
                }
            }
        });
        mServiceHelp.start(resMap,getActivity());
    }



    /*-----------------------------会员开单请求--------------------------------------------------*/
    private void openBillForMemberRequest() {
        if(!checkState()) return;
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("SETTLEMENTID", customerInfoBean.getData().getId()); //结算的id  这个是用户id
        resMap.put("PRICE", customerInfoBean.getData().getOFEE()+"");
        resMap.put("sqlType", "proc");
        resMap.put("PROID", "900.0");
        resMap.put("sqlKey",  "CP_ADD_XICHE_ORDER");
        WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadData", PubData.class,true,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                Gson gson = new Gson();
                OpenBillInfoBean openBillInfoBean = gson.fromJson(json, OpenBillInfoBean.class);
                OpenBillInfoBean.Data data = openBillInfoBean.getData();
                if( data.getOCODE().equals("00")){
                    changeButtonState();
                    Toast.makeText(getActivity(),"洗车开单成功!",Toast.LENGTH_SHORT).show();
                }else if(data.getOCODE().equals("99")){
                    Toast.makeText(getActivity(),"您不要重复开单！"+data.getOCODE(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"未知错误 code="+data.getOCODE(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        mServiceHelp.start(resMap,getActivity());
    }


    /*-----------------------------------------结算请求--------------------------------------------------*/
    private void openPayForMemberRequest(int PayWay) {
        if(!checkState()) return;
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sqlKey", "CP_SETTLEMENT_XICHE_ORDER");
        resMap.put("settlementWay", PayWay);
        resMap.put("bcid",customerInfoBean.getData().getId());
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

}
