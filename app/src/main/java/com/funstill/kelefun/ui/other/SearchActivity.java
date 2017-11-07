package com.funstill.kelefun.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.funstill.kelefun.R;

/**
 * 搜索
 *@author liukaiyang
 *@since 2017/11/7 16:56
 */
public class SearchActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("搜索");
    }
}