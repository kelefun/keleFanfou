package com.funstill.kelefun.ui.menu;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.funstill.kelefun.R;
import com.funstill.kelefun.base.BaseBackFragment;
import com.funstill.kelefun.config.AccountStore;
import com.funstill.kelefun.ui.widget.LineView;
import com.funstill.kelefun.util.SharedPreferencesUtil;

public class MenuFragmentChild extends BaseBackFragment {
    private Toolbar mToolbar;
    private LinearLayout linearLayout;
    private TextView myUsername;
    private ImageView myAvatar;

    public static MenuFragmentChild newInstance() {
        Bundle args = new Bundle();
        MenuFragmentChild fragment = new MenuFragmentChild();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_child, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("我的饭碗");
        mToolbar.inflateMenu(R.menu.my_menu);
        initToolbarNav(mToolbar);
        linearLayout = (LinearLayout) view.findViewById(R.id.my_home_linerlayout);
        myAvatar = (ImageView) view.findViewById(R.id.my_avatar);
        myUsername = (TextView) view.findViewById(R.id.my_username);
        //icon + 文字 + 箭头
        linearLayout.addView(new LineView(getContext())
                .initMine(R.drawable.menu_icon_1, "我的关注", true));
        linearLayout.addView(new LineView(getContext())
                .initMine(R.drawable.menu_icon_2, "我的粉丝", true));
        linearLayout.addView(new LineView(getContext())
                .initMine(R.drawable.menu_icon_3, "帮助反馈", true));
        linearLayout.addView(new LineView(getContext())
                .initMine(R.drawable.menu_icon_4, "检查更新", true));
        linearLayout.addView(new LineView(getContext())
                .initMine(R.drawable.menu_icon_5, "关于", true));
        linearLayout.addView(new LineView(getContext())
                .initMine(R.drawable.menu_icon_6, "设置", true));

    }

    private void initData() {
        String username = SharedPreferencesUtil.getInstance().read(getContext(), AccountStore.STORE_NAME, AccountStore.KEY_SCREEN_NAME, "我");
        myUsername.setText(username);
        String avatarUri = SharedPreferencesUtil.getInstance().read(getContext(), AccountStore.STORE_NAME, AccountStore.KEY_USER_AVATAR, "");
        if (!TextUtils.isEmpty(avatarUri)) {
            Glide.with(getContext()).load(avatarUri).into(myAvatar);
            Glide.with(getContext()).load(avatarUri).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    Drawable drawable = new BitmapDrawable(getActivity().getResources(), resource);
                    linearLayout.setBackground(drawable);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }
}