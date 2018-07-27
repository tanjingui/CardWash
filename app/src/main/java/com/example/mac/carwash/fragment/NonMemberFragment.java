package com.example.mac.carwash.fragment;

import android.app.Dialog;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.carwash.R;
import com.example.mac.carwash.activity.BaseActivity;
import com.example.mac.carwash.constants.UserInfoState;
import com.example.mac.carwash.dataInterface.getDataInterface;
import com.example.mac.carwash.jsonBean.OpenBillInfoBean;
import com.example.mac.carwash.main.record.NonmemberWashCarRecordsFragment;
import com.example.mac.carwash.util.LicenseKeyboardUtil;
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

public class NonMemberFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Spinner spinner;
   private BaseActivity mActivity;
    private LicenseKeyboardUtil keyboardUtil;
    private FragmentManager mFragmentManager;
    private FragmentTransaction transaction;
    private EditText inputbox1,inputbox2,
            inputbox3,inputbox4,
            inputbox5,inputbox6,inputbox7;
    private TextView tv_readRecords;
    public static NonMemberFragment newInstance() {
        NonMemberFragment fragment = new NonMemberFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.layout_interface_nonmember, container,false);
        init();
        return view;
    }

    public void init(){
        mActivity = (BaseActivity)getActivity();
        inputbox1 = (EditText) view.findViewById(R.id.et_car_license_inputbox1);
        inputbox2 = (EditText) view.findViewById(R.id.et_car_license_inputbox2);
        inputbox3 = (EditText) view.findViewById(R.id.et_car_license_inputbox3);
        inputbox4 = (EditText) view.findViewById(R.id.et_car_license_inputbox4);
        inputbox5 = (EditText) view.findViewById(R.id.et_car_license_inputbox5);
        inputbox6 = (EditText) view.findViewById(R.id.et_car_license_inputbox6);
        inputbox7 = (EditText) view.findViewById(R.id.et_car_license_inputbox7);
        spinner = (Spinner) view.findViewById(R.id.toolbar_spinner_select_store);
        tv_readRecords = (TextView)view.findViewById(R.id.toolbar_right_tv);
        tv_readRecords.setClickable(true);
        tv_readRecords.setOnClickListener(this);
        KeyboardView keyboardView = (KeyboardView) view.findViewById(R.id.keyboard_view);
             keyboardUtil = new LicenseKeyboardUtil(getContext(), new EditText[]{inputbox1, inputbox2, inputbox3,
                     inputbox4, inputbox5, inputbox6, inputbox7}, keyboardView, new getDataInterface() {
                 @Override
                 public void callbackResult(String result) {
                     NonMemberFragment.this.result = result;
                     customDialog(result,50);
                 }
             });
        initToolBarSpinner();
    }





   String result = "";
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.toolbar_right_tv:
                addFragment(NonmemberWashCarRecordsFragment.newInstance(),"nonmemrecords");
                break;
            default:
                break;
        }
    }


    private void addFragment(Fragment fragment,String tag){
        mFragmentManager = getActivity().getSupportFragmentManager();
        transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.content,fragment,tag);
        //将当前的事务添加到了回退栈
        transaction.addToBackStack(null);
        transaction.commit();
        transaction.hide(this);
    }


    //----------------------------------------------开单对话框----------------------------------------
    private void customDialog(final String carNum, int price) {
        String title =String.format("车牌号为 "+"<font color=#FF0000 size=20>%s</font>" +"，金额为"+"<font color=#FF0000 size=20>%s</font>元，"+ "\n确认开单？", carNum,price);
        final Dialog dialog = new Dialog(mActivity, R.style.NormalDialogStyle);
        View view = View.inflate(mActivity, R.layout.dialog_normal2, null);
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        dialog_content.setText(Html.fromHtml(title));
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(mActivity).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(mActivity).getScreenWidth() * 0.75f);
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
               openBillByCarNumRequest(carNum.substring(0,1),carNum.substring(1,carNum.length()));
                dialog.dismiss();
            }
        });
        dialog.show();
    }








    /*-----------------------------------------非会员开单请求--------------------------------------------------*/
    private void openBillByCarNumRequest(String province,String carmark) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("province",  province);
        resMap.put("store",UserInfoState.getSelectStoreCode());
        resMap.put("sqlKey", "CP_ADD_XICHE_ORDERV2");
        resMap.put("carmark", carmark);
        resMap.put("sqlType", "proc");
        WebServiceHelp mServiceHelp = new WebServiceHelp(getActivity(),"iPadService.asmx", "loadData", PubData.class,true,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                Log.i("uuu非会员录入车牌信息洗车开单回调：",""+json);
                Gson gson = new Gson();
                OpenBillInfoBean openBillInfoBean = gson.fromJson(json, OpenBillInfoBean.class);
                OpenBillInfoBean.Data data = openBillInfoBean.getData();
                if( data.getOCODE().equals("00")){
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
}
