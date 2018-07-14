package com.example.mac.carwash.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.mac.carwash.R;
import com.example.mac.carwash.adapter.ChatFragment;
import com.example.mac.carwash.adapter.ContactsFragment;
import com.example.mac.carwash.adapter.FragmentAdapter;
import com.example.mac.carwash.adapter.FriendFragment;
import com.example.mac.carwash.util.ScreenSizeUtils;
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
    private ChatFragment mChatFg;
    private FriendFragment mFriendFg;
    private ContactsFragment mContactsFg;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_interface_member, null);
        findById();
        init();
        initTabLineWidth();
        return view;
    }

    private void findById() {
        mTabContactsTv = (TextView) view.findViewById(R.id.id_contacts_tv);
        mTabChatTv = (TextView) view.findViewById(R.id.id_chat_tv);   mTabChatTv.setTextSize(18);
        mTabFriendTv = (TextView) view.findViewById(R.id.id_friend_tv);
        mTabLineIv = (ImageView) view.findViewById(R.id.id_tab_line_iv);
        mPageVp = (ViewPager) view.findViewById(R.id.toolbar_viewPager);
    }

    private void init() {
        mFriendFg = new FriendFragment();
        mContactsFg = new ContactsFragment();
        mChatFg = new ChatFragment();
        mFragmentList.add(mChatFg);
        mFragmentList.add(mFriendFg);
        mFragmentList.add(mContactsFg);

        mFragmentAdapter = new FragmentAdapter(
                getChildFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);
        mPageVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();

                Log.e("offset:", offset + "");
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



    private void customDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.NormalDialogStyle);
        View view = View.inflate(getActivity(), R.layout.dialog_normal, null);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(getActivity()).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(getActivity()).getScreenWidth() * 0.75f);
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
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}