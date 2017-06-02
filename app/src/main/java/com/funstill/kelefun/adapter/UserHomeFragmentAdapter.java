package com.funstill.kelefun.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.funstill.kelefun.ui.other.ListFragment;


public class UserHomeFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{ "消息","收藏","照片"};

    public UserHomeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return ListFragment.newInstance(mTab[position]);
            case 1:return ListFragment.newInstance(mTab[position]);
            default:return ListFragment.newInstance(mTab[position]);
        }
    }

    @Override
    public int getCount() {
        return mTab.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTab[position];
    }
}
