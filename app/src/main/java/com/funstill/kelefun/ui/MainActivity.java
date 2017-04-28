package com.funstill.kelefun.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funstill.kelefun.R;
import com.funstill.kelefun.base.BaseMainFragment;
import com.funstill.kelefun.event.TabSelectedEvent;
import com.funstill.kelefun.ui.bottombar.BottomBar;
import com.funstill.kelefun.ui.bottombar.BottomBarTab;
import com.funstill.kelefun.ui.explore.ExploreFragment;
import com.funstill.kelefun.ui.explore.ExploreFragmentChild;
import com.funstill.kelefun.ui.home.HomeLineFragment;
import com.funstill.kelefun.ui.home.HomeLineFragmentChild;
import com.funstill.kelefun.ui.send.SendStatusFragment;
import com.funstill.kelefun.ui.send.SendStatusFragmentChild;

import org.greenrobot.eventbus.EventBus;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity implements BaseMainFragment.OnBackToFirstListener {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
//    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[3];

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        EventBus.getDefault().register(this);

        if (savedInstanceState == null) {
            mFragments[FIRST] = HomeLineFragment.newInstance();
            mFragments[SECOND] = ExploreFragment.newInstance();
            mFragments[THIRD] = SendStatusFragment.newInstance();
//          mFragments[FOURTH] = HomeLineFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD]);
//                    mFragments[FOURTH]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findFragment(HomeLineFragment.class);
            mFragments[SECOND] = findFragment(ExploreFragment.class);
            mFragments[THIRD] = findFragment(SendStatusFragment.class);
//            mFragments[FOURTH] = findFragment(HomeLineFragment.class);
        }

        initView();


        // 可以监听该Activity下的所有Fragment的18个 生命周期方法
//        registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {
//
//            @Override
//            public void onFragmentSupportVisible(SupportFragment fragment) {
//                Log.i("MainActivity", "onFragmentSupportVisible--->" + fragment.getClass().getSimpleName());
//            }
//        });
    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return super.onCreateFragmentAnimator();
    }

    private void initView() {
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar
                .addItem(new BottomBarTab(this, R.drawable.ic_home_white_24dp))
                .addItem(new BottomBarTab(this, R.drawable.ic_bottomtabbar_explore))
//                .addItem(new BottomBarTab(this, R.drawable.ic_message_white_24dp))
                .addItem(new BottomBarTab(this, R.drawable.ic_bottomtabbar_add));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                SupportFragment currentFragment = mFragments[position];
                int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();

                // 如果不在该类别Fragment的主页,则回到主页;
                if (count > 1) {
                    if (currentFragment instanceof HomeLineFragment) {
                        currentFragment.popToChild(HomeLineFragmentChild.class, false);
                    } else if (currentFragment instanceof ExploreFragment) {
                        currentFragment.popToChild(ExploreFragmentChild.class, false);
                    } else if (currentFragment instanceof SendStatusFragment) {
                        currentFragment.popToChild(SendStatusFragmentChild.class, false);
                    }
//                    else if (currentFragment instanceof ZhihuFourthFragment) {
//                        currentFragment.popToChild(MeFragment.class, false);
//                    }
                    return;
                }


                // 这里推荐使用EventBus来实现 -> 解耦
                if (count == 1) {
                    // 在FirstPagerFragment中接收, 因为是嵌套的孙子Fragment 所以用EventBus比较方便
                    // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                    EventBus.getDefault().post(new TabSelectedEvent(position));
                }
            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public void onBackToFirstFragment() {
        mBottomBar.setCurrentItem(0);
    }

    /**
     * 这里暂没实现,忽略
     */
//    @Subscribe
//    public void onHiddenBottombarEvent(boolean hidden) {
//        if (hidden) {
//            mBottomBar.hide();
//        } else {
//            mBottomBar.show();
//        }
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
