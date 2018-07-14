package com.example.mac.carwash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.mac.carwash.R;
import com.example.mac.carwash.update.CheckVersionInfoTask;
import com.example.mac.carwash.util.StringUtil;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtnLogin;
    private final String jsonStr= "{\"code\":\"00\",\"data\":{\"resultset1\":[{\"QCODE\":\"1\",\"QVERSIONDESC\":\"1、UI布局优化；2、修复已知bug；3、增加新功能；\",\"QFILEPATH\":\"http://www.cetc.me/cetc5.0.14.apk\"}],\"updatecount1\":0},\"page\":null}";
    public final static String ACTION_TYPE_SERVICE = "action.type.service";
    public final static String ACTION_TYPE_THREAD = "action.type.thread";
    private LocalBroadcastManager mLocalBroadcastManager;
    private MyBroadcastReceiver mBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        //注册广播
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TYPE_SERVICE);
        intentFilter.addAction(ACTION_TYPE_THREAD);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);


        new CheckVersionInfoTask(this,jsonStr);
        initView();
    }

    public void initView(){
        mBtnLogin = (Button) findViewById(R.id.btn_load);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InItRequest();
 //             LoginUtil loginUtil = new LoginUtil(new LoginUtil.LoginInterface() {
//                    @Override
//                    public void callbackResult(int state, String stateName) {
//                        Log.i("66666666","state: "+state+" stateName: "+stateName);
//                          Intent intent = new Intent();
//                           intent.setClass(LoginActivity.this,BaseActivity.class);
//                           startActivity(intent);
//                    }
//                },"sx001","1996tjg",getApplicationContext(),"123",help);
//                loginUtil.startLogin(true);
            }
        });
    }


    // 检查更新接口.
    private void InItRequest() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("versionname", getString(R.string.app_name));
        resMap.put("locversioncode", StringUtil.getVersionCode(this));
        resMap.put("versiontype", "apk");
        resMap.put("sqlType", "proc");
        resMap.put("sqlKey", "CP_CVERSION_DETECTV2");
        WebServiceHelp mServiceHelp = new WebServiceHelp(LoginActivity.this,"iPadNoSService.asmx", "loadData", PubData.class,false,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
//                JSONObject obj = JsonUtil.toJsonObject(json);
//                String code = obj.optString("code");
                Log.i("66666",""+json);
            }
        });
        mServiceHelp.start(resMap,LoginActivity.this);
    }


    @Override
    public void onClick(View v) {

    }


    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_TYPE_SERVICE:
                   // tvServiceStatus.setText("服务状态：" + intent.getStringExtra("status"));
                    break;
                case ACTION_TYPE_THREAD:
                    int progress = intent.getIntExtra("progress", 0);
                    Log.i("客户端得到的progress",""+progress);
//                    tvThreadStatus.setText("线程状态：" + intent.getStringExtra("status"));
//                    progressBar.setProgress(progress);
//                    tvProgress.setText(progress + "%");
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }
}

