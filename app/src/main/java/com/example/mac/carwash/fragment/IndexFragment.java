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
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.activity.BaseActivity;
import com.example.mac.carwash.activity.CaptureActivity;
import com.example.mac.carwash.jsonBean.CustomerInfoBean;
import com.example.mac.carwash.jsonBean.OpenBillInfoBean;
import com.example.mac.carwash.util.Constant;
import com.example.mac.carwash.util.ScreenSizeUtils;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 2018/7/9.
 */

public class IndexFragment extends Fragment  {
    private View view;
    private Button btnQrCode;
    private Button btnWashCar;
    BaseActivity activity;
    private Spinner spinner;


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
        spinner = (Spinner) view.findViewById(R.id.spinner_select_store);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(activity, R.array.select_store,
                        R.layout.spinner_self_item);
        adapter.setDropDownViewResource(android.
                R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        btnQrCode = (Button)view.findViewById(R.id.toolbar_right_btn);
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
                customDialog("36");
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
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
             //这个bug还得继续测试------------------------------------------------------------
            //将扫描出的信息显示出来
            InItRequest(scanResult);
          //  textView.setText(scanResult);
//            Message msg = new Message();
//            msg.what = 1111;
//            msg.obj = scanResult;
//            mHandler.sendMessage(msg);
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


    private void customDialog(String price) {
        String title =String.format("订单金额"+"<font color=#FF0000 size=20>%s</font>" + "元，确认提交？", price);
        final Dialog dialog = new Dialog(getActivity(), R.style.NormalDialogStyle);
        View view = View.inflate(getActivity(), R.layout.dialog_normal, null);
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
                openBillRequest();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void changeButtonState(){
        Button button =  (Button)view.findViewById(R.id.btn_washCard);
        button.setText("已开单");
        button.setTextColor(getResources().getColor(R.color.darkslategray));
        button.setEnabled(false);
    }

    // 检查qrcode接口.
    private void InItRequest(String qrcode) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sessionId", "5269a3717f944a1c983f32b099686ddb");
        resMap.put("qrcode", qrcode);
        resMap.put("sqlKey", "CS_xiche_info_by_qrcode");
        resMap.put("sqlType", "sql");
        WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadData", PubData.class,false,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                //通知UI 关闭结束等待中的Dialog
//                Message message = new Message();
//                message.what=111;mHandler.sendMessage(message);
                //      mHandler.sendEmptyMessageDelayed(111, 2000);
                //测试json：jsonStr server：json
                Gson gson = new Gson();
                CustomerInfoBean customerInfoBean;
                customerInfoBean = gson.fromJson(json, CustomerInfoBean.class);
                CustomerInfoBean.Data data = customerInfoBean.getData();
                Message message = new Message();
                message.what=111;message.obj = data;
                BaseActivity baseActivity = (BaseActivity) getActivity();
                baseActivity.handler.sendMessage(message);
              //  Log.i("mmmmmmmmmmmmmm","  "+json);
            }
        });
        mServiceHelp.start(resMap,getActivity());
    }

    //开单请求
    private void openBillRequest() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sessionId", "54ec105bd3fc4106ad8d4ab45b6addf7");
        resMap.put("SETTLEMENTID", "1");
        resMap.put("PRICE", "0.0");
        resMap.put("sqlType", "proc");
        resMap.put("PROID", "900.0");
        resMap.put("sqlKey",  "CP_ADD_XICHE_ORDER");
        WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadData", PubData.class,false,"2");
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
                    Toast.makeText(getActivity(),"失败了，存在未结算的洗车单",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"未知错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mServiceHelp.start(resMap,getActivity());
    }


}
