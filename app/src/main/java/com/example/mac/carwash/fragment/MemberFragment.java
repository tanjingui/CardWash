package com.example.mac.carwash.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.mac.carwash.R;
import com.example.mac.carwash.constants.UserInfoState;
import com.example.mac.carwash.main.adapter.AllOrderFragment;
import com.example.mac.carwash.main.adapter.AlrePaidFragment;
import com.example.mac.carwash.main.adapter.FragmentAdapter;
import com.example.mac.carwash.main.adapter.UnPaidFragment;
import com.example.mac.carwash.main.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2018/7/10.
 */

/**
 * Created by Administrator on 2015/9/2.
 */
public class MemberFragment extends Fragment {

    public static MemberFragment newInstance() {
        MemberFragment fragment = new MemberFragment();
        return fragment;
    }


    private ViewPager mPageVp;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentAdapter mFragmentAdapter;

    //父activity fragmentManager引用
    private FragmentManager mFragmentManager;
    private FragmentTransaction transaction;

    /**
     * Tab显示内容TextView
     */
    private TextView mTabChatTv, mTabContactsTv, mTabFriendTv;
    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv;
    /**
     * Fragment
     */
    private AllOrderFragment mFragmentA;
    private UnPaidFragment mFragmentB;
    private AlrePaidFragment mFragmentC;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;
    private View view;

    //ToolBar
    private Button rightBtn;
    private Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_interface_member, null);
        findById();
        init();
        initTabLineWidth();
        return view;
    }

    private void findById() {
        rightBtn = (Button) view.findViewById(R.id.toolbar_right_btn);
        mTabContactsTv = (TextView) view.findViewById(R.id.txt_alre_settle);
        mTabChatTv = (TextView) view.findViewById(R.id.txt_all_settleState);   mTabChatTv.setTextSize(18);
        mTabFriendTv = (TextView) view.findViewById(R.id.txt_not_settle);
        mTabLineIv = (ImageView) view.findViewById(R.id.id_tab_line_iv);
        mPageVp = (ViewPager) view.findViewById(R.id.toolbar_viewPager);
        spinner = (Spinner) view.findViewById(R.id.toolbar_spinner_select_store);
    }

    private void init() {
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(SearchFragment.newInstance(getContext()));
            }
        });
        mFragmentA = AllOrderFragment.newInstance();
        mFragmentB = new UnPaidFragment();
        mFragmentC = new AlrePaidFragment();
        mFragmentList.add(mFragmentA);
        mFragmentList.add(mFragmentB);
        mFragmentList.add(mFragmentC);
        mFragmentAdapter = new FragmentAdapter(
                getChildFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);
        mPageVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();

            //    Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mTabChatTv.setTextColor(Color.RED);
                        mTabChatTv.setTextSize(18);
                        mTabFriendTv.setTextSize(15);
                        mTabContactsTv.setTextSize(15);
                        break;
                    case 1:
                        mTabFriendTv.setTextColor(Color.RED);
                        mTabFriendTv.setTextSize(18);
                        mTabChatTv.setTextSize(15);
                        mTabContactsTv.setTextSize(15);
                        break;
                    case 2:
                        mTabContactsTv.setTextColor(Color.RED);
                        mTabContactsTv.setTextSize(18);
                        mTabFriendTv.setTextSize(15);
                        mTabChatTv.setTextSize(15);
                        break;
                }
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initToolBarSpinner();
    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 3;
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mTabChatTv.setTextColor(Color.BLACK);
        mTabFriendTv.setTextColor(Color.BLACK);
        mTabContactsTv.setTextColor(Color.BLACK);
    }


    private void replaceFragment(Fragment fragment) {
        mFragmentManager =getActivity().getSupportFragmentManager();
        transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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