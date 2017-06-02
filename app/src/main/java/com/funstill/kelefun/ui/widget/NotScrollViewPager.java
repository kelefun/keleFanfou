package com.funstill.kelefun.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 禁止viewPager左右滑动
 * @author liukaiyang
 * @since 2017/5/26 20:39
 */

public class NotScrollViewPager extends ViewPager {

    public NotScrollViewPager(Context context) {
        super(context);
    }
    public NotScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
            return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
            return false;
    }
}
