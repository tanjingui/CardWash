package com.example.mac.carwash.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.mac.carwash.R;
import com.example.mac.carwash.dataInterface.getDataInterface;
import com.example.mac.carwash.fragment.IndexFragment;
import com.example.mac.carwash.fragment.MemberFragment;
import com.example.mac.carwash.fragment.NonMemberFragment;
import com.example.mac.carwash.jsonBean.CustomerInfoBean;
import com.example.mac.carwash.view.BottomPopupOption;
import com.example.mac.carwash.webservice.PubData;
import com.example.mac.carwash.webservice.WebServiceHelp;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 2018/7/9.
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BottomPopupOption.onPopupWindowItemClickListener,getDataInterface {
    private BottomNavigationBar mBottomNavigationBar;
    private FragmentManager mFragmentManager;
    private FragmentTransaction transaction;
    private BadgeItem badgeItem;
    private DrawerLayout drawer;
    private BottomPopupOption bottomPopupOption;
    //侧滑的实现
    private NavigationView navigationView;
    private boolean isDrawer=false; //判断drawer是否被划出
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 111:
                    CustomerInfoBean.Data data = (CustomerInfoBean.Data) msg.obj;
                    TextView tv_name,tv_level,tv_carNum,tv_orderPrice,tv_banl,tv_amt;
                    IndexFragment indexFragment = (IndexFragment)mFragmentManager.findFragmentByTag("index");
                    tv_name = (TextView)indexFragment.getView().findViewById(R.id.tv_info_member_name);
                    tv_level = (TextView)indexFragment.getView().findViewById(R.id.tv_info_member_level);
                    tv_carNum = (TextView)indexFragment.getView().findViewById(R.id.tv_info_car_num);
                    tv_orderPrice = (TextView)indexFragment.getView().findViewById(R.id.tv_info_order_price);
                    tv_banl = (TextView)indexFragment.getView().findViewById(R.id.tv_info_banlance);
                    tv_amt = (TextView)indexFragment.getView().findViewById(R.id.tv_info_remain);
                    Log.i("8888888",""+data.getOwner()+data.getOTYPE()+data.getOCARMARK()+data.getOFEE()+data.getOBALANCE()+data.getDiscount());
                    tv_name.setText(data.getOwner());tv_level.setText(data.getOTYPE());tv_carNum.setText(data.getOCARMARK());tv_orderPrice.setText(data.getOFEE()+"");tv_banl.setText(data.getOBALANCE()+"");tv_amt.setText(data.getDiscount()+"");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
       // InItRequest();
        initView();

    }

    public void initView(){
        replaceFragment(new IndexFragment().newInstance(),"index");
        //InItRequest("c1860707148c8db83bd53a73c7d9f6ca");
        mFragmentManager=getSupportFragmentManager();
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);//!!!自动适应
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setBarBackgroundColor(R.color.white);
        badgeItem = new BadgeItem()
                .setBackgroundColor(Color.RED).setBorderWidth(50).setText("")
                .setHideOnSelect(true); //设置被选中时隐藏角标
        mBottomNavigationBar
                .setActiveColor(R.color.red) //设置选中的颜色
                .setInActiveColor(R.color.black);//未选中

        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.icon_shop_normal, "扫码洗车"))
                .addItem(new BottomNavigationItem(R.drawable.icon_my_normal, "会员"))
                .addItem(new BottomNavigationItem(R.drawable.icon_cart_normal, "非会员"))
//                .addItem(new BottomNavigationItem(R.drawable.icon_my_normal, "个人信息"))
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        replaceFragment(new IndexFragment().newInstance(),"index");
                        //Toast.makeText(BaseActivity.this,"点击0",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        replaceFragment(new MemberFragment().newInstance(),"member");
                       // Toast.makeText(BaseActivity.this,"点击1",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        replaceFragment(new NonMemberFragment().newInstance(),"nonMember");
                       // Toast.makeText(BaseActivity.this,"点击2",Toast.LENGTH_SHORT).show();
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

        //侧滑栏的实现

        drawer = (DrawerLayout)this.findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0).findViewById(R.id.circleImage_avator);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            bottomPopupOption = new BottomPopupOption(BaseActivity.this);
            bottomPopupOption.setItemClickListener(BaseActivity.this);
            bottomPopupOption.setItemText("拍照", "选择相册");
            bottomPopupOption.showPopupWindow();
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer=true;
//                //获取屏幕的宽高
//                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//                Display display = manager.getDefaultDisplay();
//                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
//                //right.layout(left.getRight(), 0, left.getRight() + display.getWidth(), display.getHeight());
            }
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer=false;
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }


    private void replaceFragment(Fragment fragment,String tag) {
        mFragmentManager =getSupportFragmentManager();
        transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment,tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_personal_data) {
            Toast.makeText(this,"personal",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_order) {
            Toast.makeText(this,"order",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_update) {
            Toast.makeText(this,"update",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_leave) {
            Toast.makeText(this,"leave",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"你点击了"+position,Toast.LENGTH_SHORT).show();
    }






    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return false;//拦截事件
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void callbackResult(String result) {
        //对扫完二维码请求服务器返回的用户信息result，进行呈现 ，更新UI
        Gson gson = new Gson();
        CustomerInfoBean customerInfoBean;
        customerInfoBean = gson.fromJson(result, CustomerInfoBean.class);
        Message msg = handler.obtainMessage();
        CustomerInfoBean.Data data = customerInfoBean.getData();
        msg.what=111;msg.obj=data;
        handler.sendMessage(msg);
    }



  //请求洗车用户的信息
    private void InItRequest(String qrcode) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sessionId", "5269a3717f944a1c983f32b099686ddb");
        resMap.put("qrcode", qrcode);
        resMap.put("sqlKey", "CS_xiche_info_by_qrcode");
        resMap.put("sqlType", "sql");
        WebServiceHelp mServiceHelp = new WebServiceHelp(this,"iPadService.asmx", "loadData", PubData.class,false,"2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                //通知UI 关闭结束等待中的Dialog
//                Message message = new Message();
//                message.what=111;mHandler.sendMessage(message);
                //      mHandler.sendEmptyMessageDelayed(111, 2000);
                //测试json：jsonStr server：json
                Gson gson = new Gson();
                CustomerInfoBean customerInfoBean;
                customerInfoBean = gson.fromJson(json, CustomerInfoBean.class);
                CustomerInfoBean.Data data = customerInfoBean.getData();
                Message message = new Message();
                message.what=111;message.obj = data;
                handler.sendMessage(message);
                Log.i("mmmmmmmmmmmmmm","  "+json);
            }
        });
        mServiceHelp.start(resMap,this);
    }

}
