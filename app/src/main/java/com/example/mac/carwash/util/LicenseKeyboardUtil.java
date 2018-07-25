package com.example.mac.carwash.util;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mac.carwash.dataInterface.getDataInterface;
import com.example.mac.carwash.R;


/**
 * Created by xxx on 2015/9/22.
 */
public class LicenseKeyboardUtil {
    private Context ctx;
    private KeyboardView keyboardView;
    private Keyboard k1;// 省份简称键盘
    private Keyboard k2;// 数字字母键盘
    private String provinceShort[];
    private String letterAndDigit[];
    private EditText edits[];
    private int currentEditText = 0;//默认当前光标在第一个EditText

    private getDataInterface getDataInterface;

    public LicenseKeyboardUtil(Context ctx, EditText edits[],KeyboardView keyboardView,getDataInterface getDataInterface) {
        this.getDataInterface = getDataInterface;
        this.ctx = ctx;
        this.edits = edits;
        this.keyboardView = keyboardView;
        k1 = new Keyboard(ctx, R.xml.province_short_keyboard);
        k2 = new Keyboard(ctx, R.xml.lettersanddigit_keyboard);
        //keyboardView = (KeyboardView) ((Activity)ctx).findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        //设置为true时,当按下一个按键时会有一个popup来显示<key>元素设置的android:popupCharacters=""
        keyboardView.setPreviewEnabled(true);
        //设置键盘按键监听器
        keyboardView.setOnKeyboardActionListener(listener);
        setAllEditToNULL();
        edits[0].setText("赣");edits[1].setText("A");currentEditText=2;  keyboardView.setKeyboard(k2);
        provinceShort = new String[]{"京", "津", "冀", "鲁", "晋", "蒙", "辽", "吉", "黑"
                , "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘"
                , "粤", "桂", "渝", "川", "贵", "云", "藏", "陕", "甘"
                , "青", "琼", "新", "港", "澳", "台", "宁"};

        letterAndDigit = new String[]{"0","1", "2", "3", "4", "5", "6", "7", "8", "9"
                , "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"
                , "A", "S", "D", "F", "G", "H", "J", "K", "L"
                , "Z", "X", "C", "V", "B", "N", "M"};

    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onPress(int primaryCode) {
        //    LicenseKeyboardUtil.this.showKeyboard();
        }

        //currenEditText 是即将要输入的位置
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
         //   Toast.makeText(ctx,"111"+currentEditText,Toast.LENGTH_SHORT).show();
            if(primaryCode == 112){ //xml中定义的删除键值为112
                if(currentEditText <0){
                    currentEditText = 0;
                }else if(currentEditText>=0){
                    currentEditText = 0;
                    setAllEditToNULL();
                    keyboardView.setKeyboard(k1);
                }
            }else if(primaryCode == 66){ //xml中定义的完成键值为66
                if(edits[6].getText().toString().equals("")){
                    Toast.makeText(ctx,"请输入完成的车牌号！",Toast.LENGTH_SHORT).show();return;
                }
                String result="";
                for (int i=0;i<7;i++){
                    result += edits[i].getText().toString();
                }
                getDataInterface.callbackResult(result);
            }else { //其它字符按键
                if (currentEditText == 0) {
                    //如果currentEditText==0代表当前为省份键盘,
                    // 按下一个按键后,设置相应的EditText的值
                    // 然后切换为字母数字键盘
                    //currentEditText+1
                    edits[0].setText(provinceShort[primaryCode]);
                    currentEditText = 1;
                    //切换为字母数字键盘
                    keyboardView.setKeyboard(k2);
                }else{
                    //第二位必须大写字母
                    if(currentEditText == 1 && !letterAndDigit[primaryCode].matches("[A-Z]{1}")){
                        Toast.makeText(ctx,"第二位请填字母",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    edits[currentEditText].setText(letterAndDigit[primaryCode]);
                    currentEditText++;
                    if (currentEditText >=6) {
                        currentEditText = 6;
                    }
                }
            }
        }
    };

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    public void setAllEditToNULL(){
        for(int i=0;i<edits.length;i++){
            edits[i].setText("");
        }
    }
}