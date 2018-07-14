package com.example.mac.carwash.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mac.carwash.R;
import com.example.mac.carwash.activity.BaseActivity;
import com.example.mac.carwash.activity.CaptureActivity;
import com.example.mac.carwash.util.Constant;
import com.example.mac.carwash.util.ScreenSizeUtils;

/**
 * Created by mac on 2018/7/9.
 */

public class IndexFragment extends Fragment {
    private View view;
    private Button btnQrCode;
    private Button btnWashCar;
    BaseActivity activity;
    private Handler mHandler;
    TextView textView;
    public static IndexFragment newInstance(String cardCode) {
        Bundle args = new Bundle();
        args.putSerializable("cardCode",cardCode);
        IndexFragment fragment = new IndexFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_interface_index,container, false);
        activity=(BaseActivity) getActivity();
//        mHandler=activity.handler;
        initView();
        return view;
    }

    public void initView(){
        textView = (TextView) view.findViewById(R.id.tv_QRContent);
        BaseActivity activity=(BaseActivity) getActivity();
       // mHandler=activity.handler;
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
            //将扫描出的信息显示出来
            textView.setText(scanResult);
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
                Toast.makeText(activity,"提交失败",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     changeButtonState();
//                Message msg = new Message();
//                msg.what = 1111;
//                mHandler.sendMessage(msg);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void changeButtonState(){
        Button button =  (Button)view.findViewById(R.id.btn_washCard);
        button.setText("已提交");
        Toast.makeText(activity,"提交成功",Toast.LENGTH_SHORT).show();
        button.setTextColor(getResources().getColor(R.color.darkslategray));
        button.setEnabled(false);
    }
}
