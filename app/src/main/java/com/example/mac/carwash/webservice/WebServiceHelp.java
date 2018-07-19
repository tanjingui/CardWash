package com.example.mac.carwash.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mac.carwash.activity.LoginActivity;
import  com.example.mac.carwash.constants.InterfaceDefinition;
import com.example.mac.carwash.util.PreferencesUtil;
import com.example.mac.carwash.util.StringUtil;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * WebService接口调用类
 * Created by xk on 2017/7/4.
 */

public class WebServiceHelp {

    private String suffix = "iPadService.asmx";          //接口后缀
    private String methodName;
    private String json;
    private OnServiceCallBack<PubData> onServiceCallBack = null;
    private OnServiceCallBackList<PubDataList> onServiceCallBackList = null;
    private OnServiceCallBackString<String> onServiceCallBackString = null;
    private Type type;
    private Boolean isshow; //是否展示等待动画

    private ProgressDialog mDialog;

    private Context mContext;

    private String mDataType;

    /**
     * 接口数据回调接口
     * @author Pan
     * @param <T> 要获取的对象,可不填
     */
    public interface OnServiceCallBack<T>{
        /**
         * 访问接口获取到的数据
         * @param haveCallBack 是否有访问结果
         * @param zjResponse 返回的结果
         */
        public abstract void onServiceCallBack(boolean haveCallBack, PubData zjResponse);
    }

    /**
     * 接口数据回调接口
     * @author Pan
     * @param <T> 要获取的对象,可不填
     */
    public interface OnServiceCallBackList<T>{
        /**
         * 访问接口获取到的数据
         * @param haveCallBack 是否有访问结果
         * @param zjResponse 返回的结果
         */
        public abstract void onServiceCallBackList(boolean haveCallBack, PubDataList zjResponse);
    }

    /**
     * 接口数据回调接口
     * @author Pan
     */
    public interface OnServiceCallBackString<String>{
        /**
         * 访问接口获取到的数据
         * @param haveCallBack 是否有访问结果
         */
        public abstract void onServiceCallBackString(boolean haveCallBack, String json);
    }

    /**
     * 设置监听接口(开启线程之前必须调用)返回对象类型的
     * @param onServiceCallBack
     */
    public void setOnServiceCallBack(OnServiceCallBack<PubData> onServiceCallBack) {
        this.onServiceCallBack = onServiceCallBack;
    }

    /**
     * 设置监听接口(开启线程之前必须调用)返回数组类型的
     * @param onServiceCallBackList
     */
    public void setOnServiceCallBacList(OnServiceCallBackList<PubDataList> onServiceCallBackList) {
        this.onServiceCallBackList = onServiceCallBackList;
    }

    /**
     * 设置监听接口(开启线程之前必须调用)返回数组类型的
     */
    public void setOnServiceCallBackString(OnServiceCallBackString<String> onServiceCallBackString) {
        this.onServiceCallBackString = onServiceCallBackString;
    }

    /**
     * 构造方法,传人相应值
     * @param methodName 要访问的接口名称
     * @param type 要转换的对象
     *
     */
    public WebServiceHelp(Context context, String suffix, String methodName, Type type, Boolean isshow,String dataType) {
        this.type = type;
        this.methodName = methodName;
        this.suffix = suffix;
        this.isshow = isshow;
        this.mContext = context;
        this.mDataType = dataType;
    }

    //ProgressDialog动画
    private void initDialogShow() {
        if (isshow){
            mDialog = ProgressDialog.show(mContext, "", "正在请求数据，请稍候。。。");
            mDialog.show();
        }
    }

    //关闭ProgressDialog动画
    protected void dismissProgressDialog() {
        if (isshow){
            if (mDialog.isShowing()) {
                // 对话框显示中
                mDialog.dismiss();
            }
        }
    }

    /**
     * 移除监听接口(onDestroy方法中必须调用)
     */
    public void removeOnServiceCallBack() {
        if (this.onServiceCallBack != null) {
            this.onServiceCallBack = null;
        }

    }

    /**
     * 开始访问接口
     */
    public void start(Map<String,Object> data,Context mContext) {
        String sessionId = (String) PreferencesUtil.get(mContext, InterfaceDefinition.PreferencesUser.SESSIONID,"");
        data.put("sessionId", sessionId);//临时修改
        String USER_ID = (String) PreferencesUtil.get(mContext, InterfaceDefinition.PreferencesUser.USER_ID,"");
        data.put("USER_ID", USER_ID);
        this.json = JsonUtil.toJson(data);
        Log.i("uuu请求服务器的信息：：：",json+"");
        Log.e("传参====",json);
        initDialogShow();
        new Thread(new getServiceInfo()).start();
    }

    /**
     * 线程访问结果接收器
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HanderUtil.case1:
                    if (onServiceCallBack != null || onServiceCallBackList != null || onServiceCallBackString != null) {
                        if (mDataType.equals("0")){
                            onServiceCallBack.onServiceCallBack(true, (PubData) msg.obj);
                            PubData pb = (PubData) msg.obj;
                        }else if (mDataType.equals("1")){
                            PubDataList pb = (PubDataList) msg.obj;
                            onServiceCallBackList.onServiceCallBackList(true, (PubDataList) msg.obj);
                        }else if (mDataType.equals("2")){
                             String json = (String) msg.obj;
                            onServiceCallBackString.onServiceCallBackString(true,json);
                        }
                    }
                    break;
                case HanderUtil.case2:
                    if (onServiceCallBack != null) {
                        onServiceCallBack.onServiceCallBack(true, null);
                    }
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * 访问接口获取数据的线程
     * @author Administrator
     *
     */
    private class getServiceInfo implements Runnable {
        public void run() {
            //访问接口获取数据
            String result = DataUtil.callWebService(suffix, methodName, json);
            if (StringUtil.isNotEmpty(result)){
                Log.e("返回参数==",result);
                JSONObject obj = JsonUtil.toJsonObject(result);
                if (obj.optString("code").equals("-1")){    // 网络请求时登录状态失效移除相关登录状态并自动跳转到登录页面
                    PreferencesUtil.remove(mContext, InterfaceDefinition.PreferencesUser.SESSIONID);                                 // 移除SessionId.
                    PreferencesUtil.remove(mContext, InterfaceDefinition.PreferencesUser.USER_ID);                                   // 移除USER_ID.
                    PreferencesUtil.remove(mContext, InterfaceDefinition.PreferencesUser.USER_name);                               // 移除登录状态.
                    PreferencesUtil.remove(mContext, InterfaceDefinition.PreferencesUser.TERMINAL_CODE);                                      // 移除真实姓名.
                    PreferencesUtil.remove(mContext, InterfaceDefinition.PreferencesUser.Photo);                                      // 移除主车牌号码.
                    PreferencesUtil.remove(mContext, InterfaceDefinition.PreferencesUser.POSITION);                                       // 移除Vip等级.
                    PreferencesUtil.remove(mContext, InterfaceDefinition.PreferencesUser.CompanyName);          
                    Intent to = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(to);
                }
            }
            dismissProgressDialog();
            if (result != null) {
                // 获取到数据并解析
                try {
                    Message msg = new Message();
                    if (mDataType.equals("0")){
                        PubData zjResponse = JsonUtil.fromJson(result, type);
                        msg.obj = zjResponse;
                    }else if (mDataType.equals("1")){
                        PubDataList zjResponse = JsonUtil.fromJson(result, type);
                        msg.obj = zjResponse;
                    }else if (mDataType.equals("2")){
                        msg.obj = result;
                    }
                    msg.what = HanderUtil.case1;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    dismissProgressDialog();
                    //访问失败
                    mHandler.sendEmptyMessage(HanderUtil.case2);
                }
            } else {
                dismissProgressDialog();
                //访问失败
                mHandler.sendEmptyMessage(HanderUtil.case2);
            }
        }
    }
}
