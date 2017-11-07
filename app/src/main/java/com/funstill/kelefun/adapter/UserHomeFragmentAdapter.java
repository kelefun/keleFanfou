package com.funstill.kelefun.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.funstill.kelefun.ui.other.ListFragment;
import com.funstill.kelefun.ui.other.StatusListFragment;


public class UserHomeFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{"消息", "收藏", "照片"};
    private String userId;

    public UserHomeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public UserHomeFragmentAdapter(FragmentManager fm, String userId) {
        super(fm);
        this.userId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return StatusListFragment.newInstance(userId);
            case 1:
                return ListFragment.newInstance(userId);
            default:
                return ListFragment.newInstance(userId);
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
