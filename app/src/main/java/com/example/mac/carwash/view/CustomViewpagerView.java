package com.example.mac.carwash.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mac on 2018/7/11.
 */

public class CustomViewpagerView extends ViewPager {
    private int preX=0;
    public CustomViewpagerView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public CustomViewpagerView(Context context) {
        super(context);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent even) {

        if(even.getAction()==MotionEvent.ACTION_DOWN)
        {
            preX=(int) even.getX();
        }else
        {
            if(Math.abs((int)even.getX()-preX)>10)
            {
                return true;
            }else
            {
                preX=(int) even.getX();
            }
        }
        return super.onInterceptTouchEvent(even);
    }
}
