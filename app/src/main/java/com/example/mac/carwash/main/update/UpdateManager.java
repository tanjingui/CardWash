package com.example.mac.carwash.main.update;

/**
 * Created by mac on 2018/7/14.
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.activity.LoginActivity;
import com.example.mac.carwash.jsonBean.UpdateResponseInfo;
import com.example.mac.carwash.util.permissionutil.PermissionInfo;
import com.example.mac.carwash.util.permissionutil.PermissionOriginResultCallBack;
import com.example.mac.carwash.util.permissionutil.PermissionUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2018/1/2 0002.
 */

public class UpdateManager {
    private Context mContext; //上下文
   // private String apkUrl = "http://www.cetc.me/cetc5.0.14.apk"; //apk下载地址
    private static final String savePath = "/sdcard/updateAPK/"; //apk保存到SD卡的路径
    //private static final String savePath = Environment.getDataDirectory().getPath()+"/";//apk保存到SD卡的路径
    private static final String saveFileName = savePath + "apkName.apk"; //完整路径名
    private ProgressBar mProgress; //下载进度条控件
    private TextView mTxtProgress;
    private static final int DOWNLOADING = 1; //表示正在下载
    private static final int DOWNLOADED = 2; //下载完毕
    private static final int DOWNLOAD_FAILED = 3; //下载失败
    private int progress; //下载进度
    private boolean cancelFlag = false; //取消下载标志位
    //服务器的相关信息
    int apkCode;
    String updateMessage;
    String apkUrl;

    //    private double serverVersion = 2.0; //从服务器获取的版本号
//    private double clientVersion = 1.0; //客户端当前的版本号
    private String updateDescription = ""; //更新内容描述信息
    private boolean forceUpdate = true; //是否强制更新

    private AlertDialog alertDialog1, alertDialog2; //表示提示对话框、进度条对话框
   private  LoginActivity activity;
    /** 构造函数 */
    public UpdateManager(LoginActivity activity,Context context) {
        this.activity = activity;
        this.mContext = context;
    }

    /** 显示更新对话框 */
    //两个参数 double serverVersion,double clientVersion
    public void showNoticeDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
       // dialog.setTitle("发现新版本 ：" + serverVersion);
        dialog.setTitle("发现新版本");
        dialog.setMessage(updateDescription);
        dialog.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
                get(activity);

//                new PermissionDealResultCallback() {
//                    @Override
//                    public void onResult() {
//                        showDownloadDialog();
//                    }
//                };
            }
        });
        //是否强制更新
        if (forceUpdate == false) {
            dialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
        }
        alertDialog1  = dialog.create();
        alertDialog1.setCancelable(false);
        alertDialog1.show();
    }

    /** 显示进度条对话框 */
    public void showDownloadDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("正在更新");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        mTxtProgress = (TextView)v.findViewById(R.id.txt_progress);
        dialog.setView(v);
        //如果是强制更新，则不显示取消按钮
        if (forceUpdate == false) {
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                    cancelFlag = false;
                }
            });
        }
        alertDialog2  = dialog.create();
        alertDialog2.setCancelable(false);
        alertDialog2.show();

        //下载apk
        downloadAPK();
    }

    /** 下载apk的线程 */
    public void downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(savePath);
                    if(!file.exists()){
                        file.mkdir();
                    }
                    String apkFile = saveFileName;
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];

                    do{
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int)(((float)count / length) * 100);
                        //更新进度
                        mHandler.sendEmptyMessage(DOWNLOADING);
                        if(numread <= 0){
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWNLOADED);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    }while(!cancelFlag); //点击取消就停止下载.

                    fos.close();
                    is.close();
                } catch(Exception e) {
                    Log.i("777777"+e.toString(),"");
                    mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /** 更新UI的handler */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOADING:
                    mProgress.setProgress(progress);
                    mTxtProgress.setText(progress+"");
                    break;
                case DOWNLOADED:
                    if (alertDialog2 != null)
                        alertDialog2.dismiss();
                    installAPK();
                    break;
                case DOWNLOAD_FAILED:
                    Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_LONG).show();
                    break;
                case 111:
                    showDownloadDialog();
                    break;
                default:
                    break;
            }
        }
    };

    /** 下载完成后自动安装apk */
    public void installAPK() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        activity.finish();
    }


    //解析服务器传来的UpdateInfo
    public void resolveUpdateInfo(String updateJsonInfo){
        Gson gson = new Gson();
        UpdateResponseInfo info = gson.fromJson(updateJsonInfo, UpdateResponseInfo.class);
        Log.i("88888",info.toString());
        apkCode = Integer.parseInt(info.getData().getResultset1().get(0).getQCODE());
        //如果版本无更新 直接return
        if(apkCode==0){return;}
        updateDescription = info.getData().getResultset1().get(0).getQVERSIONDESC();
        apkUrl = info.getData().getResultset1().get(0).getQFILEPATH();
       if(apkCode==1){
            forceUpdate=false;
          showNoticeDialog();
        }else if(apkCode==2){
           showNoticeDialog();
        }else{
           return;
       }
        Log.i("------info------666",apkCode+"  "+updateMessage+"  "+apkUrl);
    }
    public void get(final LoginActivity activity){
        PermissionUtil.getInstance().request(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionOriginResultCallBack() {
                    @Override
                    public void onResult(List<PermissionInfo> acceptList, List<PermissionInfo> rationalList, List<PermissionInfo> deniedList) {
                        if (!acceptList.isEmpty()) {
                            Toast.makeText(activity, acceptList.get(0).getName() + " is accepted", Toast.LENGTH_SHORT).show();
                           Message message = new Message();
                            message.what=111;
                            mHandler.sendMessage(message);
                        }
                        if (!rationalList.isEmpty()) {
                            Toast.makeText(activity,"您禁止权限则无法使用该app",Toast.LENGTH_SHORT).show();
                            activity.finish();
                            //Toast.makeText(LoginActivity.this, rationalList.get(0).getName() + " is rational", Toast.LENGTH_SHORT).show();
                        }
                        if (!deniedList.isEmpty()) {
                            Toast.makeText(activity,"您禁止权限则无法使用该app",Toast.LENGTH_SHORT).show();
                            activity.finish();
                            //Toast.makeText(LoginActivity.this, deniedList.get(0).getName() + " is denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}