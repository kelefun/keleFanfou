package com.funstill.kelefun.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.funstill.kelefun.ui.notice.page.MentionsPagerFragment;
import com.funstill.kelefun.ui.notice.page.OtherPagerFragment;


public class NoticeFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{ "@我","私信","通知"};

    public NoticeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return MentionsPagerFragment.newInstance();
            case 1:return  OtherPagerFragment.newInstance(position);
            default:return  OtherPagerFragment.newInstance(position);
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
