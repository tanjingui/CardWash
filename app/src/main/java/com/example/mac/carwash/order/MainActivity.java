package com.example.mac.carwash.order;

/**
 * Created by mac on 2018/7/12.
 */
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.mac.carwash.R;
import com.example.mac.carwash.util.LicenseKeyboardUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String INPUT_LICENSE_COMPLETE = "com.example.mac.carwash.order.input.comp";
    public static final String INPUT_LICENSE_KEY = "LICENSE";

    private EditText inputbox1,inputbox2,
            inputbox3,inputbox4,
            inputbox5,inputbox6,inputbox7;
    private LinearLayout boxesContainer;
    private LicenseKeyboardUtil keyboardUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_interface_nonmember);
        inputbox1 = (EditText) this.findViewById(R.id.et_car_license_inputbox1);
        inputbox2 = (EditText) this.findViewById(R.id.et_car_license_inputbox2);
        inputbox3 = (EditText) this.findViewById(R.id.et_car_license_inputbox3);
        inputbox4 = (EditText) this.findViewById(R.id.et_car_license_inputbox4);
        inputbox5 = (EditText) this.findViewById(R.id.et_car_license_inputbox5);
        inputbox6 = (EditText) this.findViewById(R.id.et_car_license_inputbox6);
        inputbox7 = (EditText) this.findViewById(R.id.et_car_license_inputbox7);
        Button button = (Button) this.findViewById(R.id.btn_add_car);
        button.setOnClickListener(this);
        boxesContainer = (LinearLayout) this.findViewById(R.id.ll_car_license_inputbox_content);

        //输入车牌完成后的intent过滤器
        IntentFilter finishFilter = new IntentFilter(INPUT_LICENSE_COMPLETE);

        final BroadcastReceiver receiver =  new  BroadcastReceiver() {
            @Override
            public   void  onReceive(Context context, Intent intent) {
                String license = intent.getStringExtra(INPUT_LICENSE_KEY);
                if(license != null && license.length() > 0){
                    boxesContainer.setVisibility(View.GONE);
                    if(keyboardUtil != null){
                        keyboardUtil.hideKeyboard();
                    }

                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("车牌号为:" + license);
                    alertDialog = builder.create();
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                }
                MainActivity.this.unregisterReceiver(this);
            }
        };
        this.registerReceiver(receiver, finishFilter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_add_car:
                boxesContainer.setVisibility(View.VISIBLE);
                keyboardUtil = new LicenseKeyboardUtil(this,new EditText[]{inputbox1,inputbox2,inputbox3,
                        inputbox4,inputbox5,inputbox6,inputbox7});
                keyboardUtil.showKeyboard();
                break;
        }
    }
}