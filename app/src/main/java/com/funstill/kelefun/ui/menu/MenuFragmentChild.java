package com.funstill.kelefun.ui.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funstill.kelefun.R;
import com.funstill.kelefun.base.BaseBackFragment;

public class MenuFragmentChild extends BaseBackFragment {
//    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    public static MenuFragmentChild newInstance() {
        Bundle args = new Bundle();
        MenuFragmentChild fragment = new MenuFragmentChild();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_container, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        initToolbarNav(mToolbar);
        mCollapsingToolbarLayout=(CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}