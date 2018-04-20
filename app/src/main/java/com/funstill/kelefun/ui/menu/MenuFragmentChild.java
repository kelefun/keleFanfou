package com.funstill.kelefun.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;
import com.funstill.kelefun.base.BaseBackFragment;
import com.funstill.kelefun.config.AccountStore;
import com.funstill.kelefun.data.api.UserApi;
import com.funstill.kelefun.data.model.UserInfo;
import com.funstill.kelefun.http.BaseRetrofit;
import com.funstill.kelefun.http.SignInterceptor;
import com.funstill.kelefun.ui.LoginActivity;
import com.funstill.kelefun.ui.userhome.UserHomeActivity;
import com.funstill.kelefun.ui.widget.LineView;
import com.funstill.kelefun.util.SharedPreferencesUtil;
import com.funstill.kelefun.util.ToastUtil;
import com.github.glomadrian.grav.GravView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.funstill.kelefun.config.KelefunConst.USER_ID;

public class MenuFragmentChild extends BaseBackFragment implements LineView.OnLineClickListener {
    private final String ATTENTION = "我的关注", FANS = "我的粉丝", HELP = "帮助反馈", UPDATE = "检查更新", SETUP = "设置", ABOUT = "关于";
    private Toolbar mToolbar;
    private LinearLayout linearLayout;
    private TextView myUsername;
    private ImageView myAvatar;
    private ImageView myProfileEdit;
    private Button logoutButton;
    private GravView gravView;
    private boolean isRunning = true;

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
        initUserInfo();
        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("我的饭碗");
        mToolbar.inflateMenu(R.menu.my_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exchange_account:
                        ToastUtil.showToast(getContext(), "切换账号");
                        break;
                    default:
                        ToastUtil.showToast(getContext(), "error menu");
                        break;
                }
                return true;

            }
        });
//        initToolbarNav(mToolbar);
        linearLayout = (LinearLayout) view.findViewById(R.id.my_home_linerlayout);
        logoutButton = (Button) view.findViewById(R.id.logout_button);
        myAvatar = (ImageView) view.findViewById(R.id.my_avatar);
        myProfileEdit = (ImageView) view.findViewById(R.id.my_profile_edit);
        myUsername = (TextView) view.findViewById(R.id.my_username);
        gravView = (GravView) view.findViewById(R.id.grav);
        //icon + 文字 + 箭头
        linearLayout.addView(new LineView(getContext())
                .init(R.drawable.menu_icon_1, ATTENTION, true)
                .setOnRootClickListener(this, ATTENTION));
        linearLayout.addView(new LineView(getContext())
                .init(R.drawable.menu_icon_2, FANS, true)
                .setOnRootClickListener(this, FANS));
        linearLayout.addView(new LineView(getContext())
                .init(R.drawable.menu_icon_3, HELP, true)
                .setOnRootClickListener(this, HELP));
        linearLayout.addView(new LineView(getContext())
                .init(R.drawable.menu_icon_4, UPDATE, true)
                .setOnRootClickListener(this, UPDATE));
        linearLayout.addView(new LineView(getContext())
                .init(R.drawable.menu_icon_5, ABOUT, true)
                .setOnRootClickListener(this, ABOUT));
        linearLayout.addView(new LineView(getContext())
                .init(R.drawable.menu_icon_6, SETUP, true)
                .setOnRootClickListener(this, SETUP));
        logoutButton.setOnClickListener((v) -> {
            new AlertDialog.Builder(getContext())
                    .setMessage("退出登录")
                    .setNegativeButton(android.R.string.cancel,
                            (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(android.R.string.ok,
                            (dialog, which) -> {
                                logout();
                                dialog.dismiss();
                            })
                    .show();
        });
        myProfileEdit.setOnClickListener((v) -> {
            ToastUtil.showToast(getContext(), "个人资料编辑");
        });
        myAvatar.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), UserHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(USER_ID, SharedPreferencesUtil.getInstance().read(getContext(), AccountStore.STORE_NAME, AccountStore.KEY_USER_ID, ""));
            getContext().startActivity(intent);
        });

    }

    private void setUserFromPref() {
        String username = SharedPreferencesUtil.getInstance().read(getContext(), AccountStore.STORE_NAME, AccountStore.KEY_SCREEN_NAME, "我");
        myUsername.setText(username);
        String avatarUri = SharedPreferencesUtil.getInstance().read(getContext(), AccountStore.STORE_NAME, AccountStore.KEY_USER_AVATAR, "");
        if (!TextUtils.isEmpty(avatarUri)) {
            Glide.with(getContext()).load(avatarUri).into(myAvatar);
//            Glide.with(getContext()).load(avatarUri).asBitmap().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                    Drawable drawable = new BitmapDrawable(getActivity().getResources(), resource);
//                    linearLayout.setBackground(drawable);
//                }
//            });
        }
    }

    /**
     * 获取用户信息
     */
    private void initUserInfo() {
        UserApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(UserApi.class);
        Call<UserInfo> call = api.getUsersShow(null);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.code() == 200) {
                    UserInfo userInfo = response.body();
                    if (userInfo != null) {
                        myUsername.setText(userInfo.getScreenName());
                        Glide.with(getContext()).load(userInfo.getProfileImageUrlLarge()).into(myAvatar);
                    } else {
                        setUserFromPref();
                    }
                } else {
                    setUserFromPref();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                setUserFromPref();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isRunning) {//TODO start没用,grav组件的bug
            gravView.start();
            isRunning = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gravView.stop();
        isRunning = false;
    }

    @Override
    public void onLineClick(View view) {
        switch ((String) view.getTag()) {
            case ATTENTION:
                ToastUtil.showToast(getContext(), ATTENTION);
                break;
            case FANS:
                ToastUtil.showToast(getContext(), FANS);
                break;
            case HELP:
                ToastUtil.showToast(getContext(), HELP);
                break;
            case UPDATE:
                ToastUtil.showToast(getContext(), UPDATE);
                break;
            case SETUP:
                ToastUtil.showToast(getContext(), SETUP);
                break;
            case ABOUT:
                ToastUtil.showToast(getContext(), ABOUT);
                break;
            default:
                ToastUtil.showToast(getContext(), "error");
        }
    }

    private void logout() {
        SharedPreferencesUtil.getInstance().removeAll(getContext(), AccountStore.STORE_NAME);
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }
}