package com.example.mac.carwash.activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mac.carwash.R;
import com.example.mac.carwash.main.update.UpdateManager;
import com.example.mac.carwash.util.StringUtil;
import com.example.mac.carwash.util.WeiboDialogUtils;
import com.example.mac.carwash.webservice.JsonUtil;
import com.example.mac.carwash.webservice.LoginUtil;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtnLogin;
    private final String jsonStr= "{\"code\":\"00\",\"data\":{\"resultset1\":[{\"QCODE\":\"1\",\"QVERSIONDESC\":\"1、UI布局优化；2、修复已知bug；3、增加新功能；\",\"QFILEPATH\":\"http://www.cetc.me/cetc5.0.14.apk\"}],\"updatecount1\":0},\"page\":null}";
    private Dialog mWeiboDialog;
    private EditText mUsername;
    private EditText mPassword;
    private WebServiceHelp mServiceHelp;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(LoginActivity.this, "加载中...");
          InItRequest();
        //离线下模拟
        //  new UpdateManager(this,this).resolveUpdateInfo(jsonStr);
        initView();
    }

    public void initView(){
        mServiceHelp = new WebServiceHelp(LoginActivity.this,"iNoSService.asmx", "loadData", PubData.class,false,"0");
        mBtnLogin = (Button) findViewById(R.id.btn_load);
        mUsername = (EditText)findViewById(R.id.edit_userAccount);
        mPassword = (EditText)findViewById(R.id.edit_password);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUtil loginUtil  = new LoginUtil(new LoginUtil.LoginInterface() {
                    @Override
                    public void callbackResult(int state, String stateName) {
                        if (state == 0) {
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,BaseActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(LoginActivity.this,""+stateName,Toast.LENGTH_SHORT).show();
                        }
                    }
                },"sx001","1996tjg",LoginActivity.this,"1", mServiceHelp);
                loginUtil.startLogin(true);

            }
        });
    }


    // 检查登录接口
    private void InItRequest() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("versionname", StringUtil.getVersionName(this));
        resMap.put("locversioncode", StringUtil.getVersionCode(this));
        resMap.put("versiontype", "apk");
        resMap.put("sqlType", "proc");
        resMap.put("sqlKey", "CP_CVERSION_DETECTV2");
        WebServiceHelp mServiceHelp = new WebServiceHelp(LoginActivity.this,"iPadNoSService.asmx", "loadData", PubData.class,false,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                //通知UI 关闭结束等待中的Dialog
                Message message = new Message();
                message.what=111;mHandler.sendMessage(message);
         //      mHandler.sendEmptyMessageDelayed(111, 2000);
                //测试json：jsonStr server：json
                JSONObject obj = JsonUtil.toJsonObject(json);
                String code = obj.optString("code");
                if(!code.equals("00")){
                    //接口异常，则直接退出app
                    Toast.makeText(LoginActivity.this,"接口异常",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    new UpdateManager(LoginActivity.this,LoginActivity.this).resolveUpdateInfo(json);
                }
                Log.i("66666",""+code+"  "+json);
            }
        });
        mServiceHelp.start(resMap,LoginActivity.this);
    }


    @Override
    public void onClick(View v) {

    }
}

