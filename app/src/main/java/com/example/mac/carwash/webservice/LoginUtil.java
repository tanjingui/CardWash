package com.example.mac.carwash.webservice;

import android.content.Context;

import com.example.mac.carwash.R;
import com.example.mac.carwash.constants.InterfaceDefinition;
import com.example.mac.carwash.util.APKVersionCodeUtils;
import com.example.mac.carwash.util.PreferencesUtil;
import com.example.mac.carwash.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 对短信登录与账号密码登录进行封装.
 *
 * @author
 *              ljh
 *
 * Created by Administrator on 2017/9/20.
 */
public class LoginUtil {
    private String userName;
    private String passWord;
    private Context context;
    private String versionType;
    private WebServiceHelp webServiceHelp;
    private LoginInterface loginInterface;

    public interface LoginInterface {
        void callbackResult(int state, String stateName);
    }

    public LoginUtil(LoginInterface callback, String name, String psd, Context cxt, String type, WebServiceHelp help) {
        this.context = cxt;
        this.passWord = psd;
        this.userName = name;
        this.versionType = type;
        this.webServiceHelp = help;
        this.loginInterface = callback;
    }

    public void startLogin(Boolean showProcess) {
        Map<String, Object> map = new HashMap<>();
        map.put("LOGINNAME", userName);
        map.put("PWD", passWord);
        map.put("deviceId",  StringUtil.getDeviceId(context));
        map.put("DEVICETYPE", StringUtil.getDeviceType());
        map.put("OSVERSION", StringUtil.getOsVersion());
        map.put("CLIENTVERSIONCODE", APKVersionCodeUtils.getVersionCode(context) + "");
        map.put("VERSIONSTYLE", versionType);
        map.put("VERSIONTYPE", "apk");
        map.put("VERSIONNAME", context.getString(R.string.app_name));
        webServiceHelp = new WebServiceHelp(context, "iPadService.asmx", "login", PubData.class,showProcess,"2");
        webServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                removeUserInfo();
                JSONObject obj = JsonUtil.toJsonObject(json);
                int code = obj.optInt("code");
                if (code == 0) {
                    JSONObject data = JsonUtil.toJsonObject(obj.optString("data"));
                    JSONArray userInfo = JsonUtil.toJsonArray(data.optString("USERINFO"));
                    JSONArray gafSession = JsonUtil.toJsonArray(data.optString("GafSession"));
                    JSONObject dg1 = userInfo.optJSONObject(0);
                    JSONObject dg2 = gafSession.optJSONObject(0);
                    PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.USER_loginname, dg1.optString("LOGIN_NAME"));           // 存登录账号.
                    PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.SESSIONID,obj.optString("sessionId"));      // 存SessionId.
                    PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.USER_ID, dg2.optString("USER_ID"));                      // 存登录账号.
                    PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.USER_name, dg1.optString("USER_name"));                      // 存登录账号.
                    PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.TERMINAL_CODE, dg1.optString("TERMINAL_CODE"));     // 车账户ID.
                    PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.Photo,dg1.optString("LOGOIMG"));                           // 存登录状态.
                    PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.POSITION, dg1.optString("POSITION"));               // 存真实姓名.
                    PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.CompanyName, dg2.optString("COMNAME"));           // 存主车牌号码.

                    if (null != loginInterface) {
                        loginInterface.callbackResult(code, context.getString(R.string.login_message_text4));
                    }
                    return;
                }
                else if (code == 29) {
                    if (null != loginInterface) {
                        loginInterface.callbackResult(code, context.getString(R.string.login_message_text5));
                    }
                }
                else if (code == 19) {
                    if (null != loginInterface) {
                        loginInterface.callbackResult(code, context.getString(R.string.login_message_text6));
                    }
                }
                else if (code == 9) {
                    if (null != loginInterface) {
                        loginInterface.callbackResult(code, context.getString(R.string.login_message_text7));
                    }
                }
                else {
                    if (null != loginInterface) {
                        loginInterface.callbackResult(code, context.getString(R.string.login_message_text8));
                    }
                }
            }
        });
        webServiceHelp.start(map, context);
    }

    public void removeUserInfo()
    {
        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.SESSIONID,"");      // 存SessionId.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.USER_PHONE, "");                      // 存登录账号.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.pwd, "");                      // 存登录账号.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.ACCOUNTID, "");     // 车账户ID.
        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.USER_ID,"");         // 存USER_ID.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.LOGIN_STATE,false);                          // 存登录状态.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.NAME, "");               // 存真实姓名.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.PHOTO, "");             // 存用户头像.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.CARNUM, "");              // 存主车牌号码.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.CARID, "");             // 存主ID.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.VIP, "");                 // 存Vip等级.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.CARPHOTO, "");       // 存主账号汽车头像.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.STARNUM, "");         // 存账号是否认证0，8是未认证.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.AUTHENTICATION_STATE, "");
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.AUTHENTICATION_RESULT, ""); // 实名认证的结果.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.ID, "");              // 当前认证的身份证号.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.FRONT_ID_PHOTO, "");    // 用户正面身份证照的地址
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.BACK_ID_PHOTO, "");     // 用户背面身份证照的地址.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.Merge_ID_PHOTO, "");    // 手持身份证正面照的地址.
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.VIP_TYPE, "");
//        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.Member_Type_Name_App, "");
        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.USER_name,"");
        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.CompanyName,"");
        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.Photo,"");
        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.POSITION,"");
        PreferencesUtil.put(context, InterfaceDefinition.PreferencesUser.TERMINAL_CODE,"");
    }
}
