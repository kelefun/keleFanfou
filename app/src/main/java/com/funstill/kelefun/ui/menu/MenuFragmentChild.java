package com.funstill.kelefun.ui.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funstill.kelefun.R;
import com.funstill.kelefun.base.BaseBackFragment;
import com.funstill.kelefun.event.TabSelectedEvent;
import com.funstill.kelefun.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MenuFragmentChild extends BaseBackFragment {
//    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    //是否滑动到顶部
    private boolean mInAtTop = true;

    public static MenuFragmentChild newInstance() {
        Bundle args = new Bundle();
        MenuFragmentChild fragment = new MenuFragmentChild();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        initToolbarNav(mToolbar);
        mCollapsingToolbarLayout=(CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
    }


    /**
     * 选择tab事件
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainActivity.FIRST) return;

        if (!mInAtTop) {
//            scrollToTop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}