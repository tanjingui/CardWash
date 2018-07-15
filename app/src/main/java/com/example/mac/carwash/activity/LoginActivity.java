package com.example.mac.carwash.activity;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.update.UpdateManager;
import com.example.mac.carwash.util.StringUtil;
import com.example.mac.carwash.util.permissionutil.PermissionInfo;
import com.example.mac.carwash.util.permissionutil.PermissionOriginResultCallBack;
import com.example.mac.carwash.util.permissionutil.PermissionUtil;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtnLogin;
    private final String jsonStr= "{\"code\":\"00\",\"data\":{\"resultset1\":[{\"QCODE\":\"2\",\"QVERSIONDESC\":\"1、UI布局优化；2、修复已知bug；3、增加新功能；\",\"QFILEPATH\":\"http://www.cetc.me/cetc5.0.14.apk\"}],\"updatecount1\":0},\"page\":null}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        new UpdateManager(this).resolveUpdateInfo(jsonStr);
        initView();
    }

    public void initView(){
        mBtnLogin = (Button) findViewById(R.id.btn_load);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,BaseActivity.class);
                startActivity(intent);
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

    public void get(LoginActivity activity){
        PermissionUtil.getInstance().request(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionOriginResultCallBack() {
                    @Override
                    public void onResult(List<PermissionInfo> acceptList, List<PermissionInfo> rationalList, List<PermissionInfo> deniedList) {
                        if (!acceptList.isEmpty()) {
                            Toast.makeText(LoginActivity.this, acceptList.get(0).getName() + " is accepted", Toast.LENGTH_SHORT).show();
                        }
                        if (!rationalList.isEmpty()) {
                            Toast.makeText(LoginActivity.this,"您禁止权限则无法使用该app",Toast.LENGTH_SHORT).show();
                            finish();
                            //Toast.makeText(LoginActivity.this, rationalList.get(0).getName() + " is rational", Toast.LENGTH_SHORT).show();
                        }
                        if (!deniedList.isEmpty()) {
                            Toast.makeText(LoginActivity.this,"您禁止权限则无法使用该app",Toast.LENGTH_SHORT).show();
                            finish();
                            //Toast.makeText(LoginActivity.this, deniedList.get(0).getName() + " is denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

