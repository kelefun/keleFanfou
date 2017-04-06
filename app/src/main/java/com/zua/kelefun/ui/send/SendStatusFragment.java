package com.zua.kelefun.ui.send;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zua.kelefun.R;
import com.zua.kelefun.base.BaseMainFragment;
import com.zua.kelefun.ui.send.child.SendStatusFragmentChild;

public class SendStatusFragment extends BaseMainFragment {

    public static SendStatusFragment newInstance() {
        Bundle args = new Bundle();
        SendStatusFragment fragment = new SendStatusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_status, container, false);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_send_status_container, SendStatusFragmentChild.newInstance());
        }
    }
}
