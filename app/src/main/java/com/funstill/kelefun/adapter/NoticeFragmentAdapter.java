package com.funstill.kelefun.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.funstill.kelefun.ui.notice.page.MentionsPagerFragment;
import com.funstill.kelefun.ui.notice.page.MsgPagerFragment;
import com.funstill.kelefun.ui.notice.page.OtherPagerFragment;


public class NoticeFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{ "私信","@我","通知"};

    public NoticeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return MsgPagerFragment.newInstance();
            case 1:return  MentionsPagerFragment.newInstance();
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
