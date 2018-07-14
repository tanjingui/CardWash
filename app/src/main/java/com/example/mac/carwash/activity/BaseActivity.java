package com.example.mac.carwash.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.mac.carwash.R;
import com.example.mac.carwash.fragment.IndexFragment;
import com.example.mac.carwash.fragment.MemberFragment;
import com.example.mac.carwash.fragment.NonMemberFragment;

/**
 * Created by mac on 2018/7/9.
 */

public class BaseActivity extends AppCompatActivity {
    private BottomNavigationBar mBottomNavigationBar;
    FragmentManager mFragmentManager;
    FragmentTransaction transaction;
    BadgeItem badgeItem;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1111) {

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        initView();
    }

    public void initView(){
        replaceFragment(new IndexFragment().newInstance("1111"));
        mFragmentManager=getSupportFragmentManager();
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setBarBackgroundColor(R.color.white);
        badgeItem = new BadgeItem()
                .setBackgroundColor(Color.RED).setText("99")
                .setHideOnSelect(true); //设置被选中时隐藏角标
        mBottomNavigationBar
                .setActiveColor(R.color.red) //设置选中的颜色
                .setInActiveColor(R.color.gray);//未选中

        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.icon_shop_normal, "扫码洗车"))
                .addItem(new BottomNavigationItem(R.drawable.icon_my_normal, "会员"))
                .addItem(new BottomNavigationItem(R.drawable.icon_cart_normal, "非会员"))
                .addItem(new BottomNavigationItem(R.drawable.icon_my_normal, "个人信息"))
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        replaceFragment(new IndexFragment().newInstance("2222"));
                        Toast.makeText(BaseActivity.this,"点击0",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        replaceFragment(new MemberFragment().newInstance());
                        Toast.makeText(BaseActivity.this,"点击1",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        replaceFragment(new NonMemberFragment().newInstance());
                        Toast.makeText(BaseActivity.this,"点击2",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });


    }





    private void replaceFragment(Fragment fragment) {
        mFragmentManager =getSupportFragmentManager();
        transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
