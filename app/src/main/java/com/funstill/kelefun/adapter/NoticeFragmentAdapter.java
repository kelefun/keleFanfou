package com.funstill.kelefun.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.funstill.kelefun.ui.notice.page.FirstPagerFragment;
import com.funstill.kelefun.ui.notice.page.OtherPagerFragment;


/**
 * Created by YoKeyword on 16/6/5.
 */
public class NoticeFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{ "@我","私信", "通知"};

    public NoticeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FirstPagerFragment.newInstance();
        } else {
            return OtherPagerFragment.newInstance(position);
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
