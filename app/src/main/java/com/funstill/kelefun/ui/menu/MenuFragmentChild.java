package com.funstill.kelefun.ui.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.funstill.kelefun.R;
import com.funstill.kelefun.base.BaseBackFragment;
import com.funstill.kelefun.ui.widget.LineView;

public class MenuFragmentChild extends BaseBackFragment {
    //    private Toolbar mToolbar;
    private LinearLayout linearLayout;

    public static MenuFragmentChild newInstance() {
        Bundle args = new Bundle();
        MenuFragmentChild fragment = new MenuFragmentChild();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_child, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//       initToolbarNav(mToolbar);
        linearLayout = (LinearLayout) view.findViewById(R.id.my_home_linerlayout);
        //icon + 文字 + 箭头
        linearLayout.addView(new LineView(getContext())
                .initMine(R.mipmap.ic_launcher, "第一行", true));
        linearLayout.addView(new LineView(getContext())
                .initMine(R.mipmap.ic_launcher, "第2行", true));
        linearLayout.addView(new LineView(getContext())
                .initMine(R.mipmap.ic_launcher, "第3行", true));
        linearLayout.addView(new LineView(getContext())
                .initMine(R.mipmap.ic_launcher, "第4行", true));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}