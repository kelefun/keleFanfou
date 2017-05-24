package com.funstill.kelefun.ui.notice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funstill.kelefun.R;
import com.funstill.kelefun.base.BaseMainFragment;

public class NoticeFragment extends BaseMainFragment {

    public static NoticeFragment newInstance() {
        Bundle args = new Bundle();
        NoticeFragment fragment = new NoticeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_container, container, false);
        initView(savedInstanceState);
        return view;
    }

    private void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_notice_container, ViewPagerFragment.newInstance());
        }
    }
//    @Override
//    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
//        super.onLazyInitView(savedInstanceState);
//        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create
//    }
}
