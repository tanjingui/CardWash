package com.example.mac.carwash.update;

/**
 * Created by mac on 2018/7/14.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.jsonBean.UpdateResponseInfo;
import com.google.gson.Gson;

/**
 *
 */
public class CheckVersionInfoTask  {

    private static final String TAG = "CheckVersionInfoTask";
    private Context mContext;
    private ProgressDialog progressDialog;
    int apkCode;
    String updateMessage;
    String apkUrl;

    public CheckVersionInfoTask(Context context, String jsonStr) {
        this.mContext = context;
        resolveUpdateInfo(jsonStr);
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                progressDialog.setProgress(msg.arg1);
            }
        }
    };
    /**
     *
     * 判断是否需要更新 && 是否执行强制更新
     */
    private void judgeUpdate(){
            //取得已经安装在手机的APP的版本号 versionCode
            int versionCode = getCurrentVersionCode();
            Log.i("versionCode66666",""+versionCode);
//            if (apkCode > versionCode) {
//                showDialog(updateMessage, apkUrl);
//                showForceDialog(updateMessage, apkUrl);
//
//            }
        if (apkCode==1) {
                showDialog(updateMessage, apkUrl);
            }else if(apkCode==2){
            showForceDialog(updateMessage,apkUrl);
            }
            else{
                Toast.makeText(mContext, "无需更新", Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * 取得当前版本号
     * @return
     */
    public int getCurrentVersionCode() {

        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return 0;
    }


    /**
     * 显示对话框提示用户有新版本，并且让用户选择是否更新版本
     * @param content
     * @param downloadUrl
     */
    public void showDialog(String content, final String downloadUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.dialog_choose_update_title);
        builder.setMessage(Html.fromHtml(content))
                .setPositiveButton(R.string.dialog_btn_confirm_download, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //下载apk文件
                        goToDownloadApk(downloadUrl);
                    }
                })
                .setNegativeButton(R.string.dialog_btn_cancel_download, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog dialog = builder.create();
        //点击对话框外面,对话框不消失 必须进行选择
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showForceDialog(String content, final String downloadUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.dialog_choose_update_title);
        builder.setMessage(Html.fromHtml(content))
                .setPositiveButton(R.string.dialog_btn_confirm_download, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //下载apk文件
                        goToDownloadApk(downloadUrl);
                    }
                });
        AlertDialog dialog = builder.create();
        //点击对话框外面,对话框不消失 必须进行选择
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public void showDownloadDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

     //   progressDialog.dismiss();
    }






    /**
     * 用intent启用DownloadService服务去下载AKP文件
     * @param downloadUrl
     */
    private void goToDownloadApk(String downloadUrl) {
        Intent intent = new Intent(mContext, DownloadApkService.class);
        intent.putExtra("apkUrl", downloadUrl);
        mContext.startService(intent);
    }

   //解析服务器传来的UpdateInfo
    public void resolveUpdateInfo(String updateJsonInfo){
        Gson gson = new Gson();
        UpdateResponseInfo info = gson.fromJson(updateJsonInfo, UpdateResponseInfo.class);
        apkCode = Integer.parseInt(info.getData().getResultset1().get(0).getQCODE());
        updateMessage = info.getData().getResultset1().get(0).getQVERSIONDESC();
        apkUrl = info.getData().getResultset1().get(0).getQFILEPATH();
        Log.i("------info------666",apkCode+"  "+updateMessage+"  "+apkUrl);
        judgeUpdate();
    }
}