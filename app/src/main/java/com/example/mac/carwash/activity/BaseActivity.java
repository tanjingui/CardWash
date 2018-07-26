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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mac.carwash.R;
import com.example.mac.carwash.constants.InterfaceDefinition;
import com.example.mac.carwash.constants.UserInfoState;
import com.example.mac.carwash.dataInterface.getDataInterface;
import com.example.mac.carwash.fragment.IndexFragment;
import com.example.mac.carwash.fragment.MemberFragment;
import com.example.mac.carwash.fragment.NonMemberFragment;
import com.example.mac.carwash.jsonBean.CustomerInfoBean;
import com.example.mac.carwash.jsonBean.StoreInfoBean;
import com.example.mac.carwash.util.PreferencesUtil;
import com.example.mac.carwash.view.BottomPopupOption;
import com.example.mac.carwash.webservice.PubDataList;
import com.example.mac.carwash.webservice.WebServiceHelp;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 2018/7/9.
 */

    public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BottomPopupOption.onPopupWindowItemClickListener,getDataInterface {
        private BottomNavigationBar mBottomNavigationBar;  //底部导航栏
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
                    tv_name.setText(data.getOwner());tv_level.setText(data.getOTYPE());tv_carNum.setText(data.getOCARMARK());tv_orderPrice.setText(data.getOFEE()+"元");tv_banl.setText(data.getOBALANCE()+"元");tv_amt.setText(data.getDiscount()+"次");
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
        initView();  //初始化layout各种控件  将得到信息填充
        //请求门店信息， 再去初始化加载内部fragment布局 && 只请求一次
        if(UserInfoState.getSelectStoreIndex()==-1){
             acquireStoreInfo();}
    }

    public void initView(){
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
                .addItem(new BottomNavigationItem(R.drawable.icon_my_normal, "记录"))
                .addItem(new BottomNavigationItem(R.drawable.icon_cart_normal, "输入车牌"))
                //.setFirstSelectedPosition(1)  第一个默认呈现的item
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
        CircleImageView headerView = (CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.circleImage_avator);
        TextView tv1 = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        TextView tv2 = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_post);
        tv1.setText((String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.USER_name,""));
        tv2.setText((String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.POSITION,""));
        Glide.with(this)
                .load((String) PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.URL,"")+ PreferencesUtil.get(this, InterfaceDefinition.PreferencesUser.Photo,""))
                .error(R.mipmap.default_avator)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .centerCrop()
                .into(headerView);
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

    //记录用户首次点击返回键的时间
    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime=System.currentTimeMillis();
                if(secondTime-firstTime>2000){
                    Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    firstTime=secondTime;
                    return true;
                }else{
                    System.exit(0);
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }




    //在一进入主界面BaseActivity就需要去 请求门店信息
    /*-----------------------------------------传入version，请求门店信息--------------------------------------------------*/
    private void acquireStoreInfo() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("sqlKey", "SS_CETC_shop_list");
        resMap.put("sqlType", "sql");
        WebServiceHelp mServiceHelp = new WebServiceHelp(this, "iPadService.asmx", "loadDataList", PubDataList.class, false, "2");
        mServiceHelp.setOnServiceCallBackString(new WebServiceHelp.OnServiceCallBackString<String>() {
            @Override
            public void onServiceCallBackString(boolean haveCallBack, String json) {
                Gson gson = new Gson();
                StoreInfoBean storeInfoBean;
               Map<String,String>map = new LinkedHashMap<String, String>(){};
                storeInfoBean = gson.fromJson(json,StoreInfoBean.class);
                List<StoreInfoBean.Data> list = storeInfoBean.getData();
                for(StoreInfoBean.Data item: list) {
                    String deptName = item.getDeptName();
                    String deptCode = item.getDEPT_CODE();
                    map.put(deptName,deptCode);
                }
                UserInfoState.setStoreMap(map);
                UserInfoState.setSelectStoreIndex(0);  //默认选择第一项
                UserInfoState.setSelectStoreCode(list.get(0).getDEPT_CODE());
                //获取到所有初始化布局的信息后，再去加载UI，填充界面 && 先显示会员订单界面
                replaceFragment(IndexFragment.newInstance(),"index");
            }
        });
        mServiceHelp.start(resMap, this);
    }
}
