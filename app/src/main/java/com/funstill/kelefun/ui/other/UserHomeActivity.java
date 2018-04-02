package com.funstill.kelefun.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;
import com.funstill.kelefun.adapter.UserHomeFragmentAdapter;
import com.funstill.kelefun.data.api.FriendShipApi;
import com.funstill.kelefun.data.api.UserApi;
import com.funstill.kelefun.data.model.UserInfo;
import com.funstill.kelefun.http.BaseRetrofit;
import com.funstill.kelefun.http.SignInterceptor;
import com.funstill.kelefun.util.DateUtil;
import com.funstill.kelefun.util.LogHelper;
import com.funstill.kelefun.util.ToastUtil;
import com.jaeger.library.StatusBarUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.funstill.kelefun.config.KelefunConst.USER_ID;

/**
 * 用户主页
 *
 * @author liukaiyang
 * @since 2017/10/18 16:56
 */
public class UserHomeActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView description;
    private TextView location;
    private TextView reg_time;
    private TextView friendsCount;
    private TextView followersCount;
    private TextView statusesCount;
    private ImageView profileBackgroundImage;
    private ImageView profileImage;
    private MenuItem attentionRequest;//请求关注
    private MenuItem attentionCancel;//已关注
    private static final int ALPHA = 32;//透明度
    private TabLayout userHomeTab;
    private ViewPager mViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        StatusBarUtil.setTranslucent(this, ALPHA);
        initTab();
        initView();
        getUserInfo();
    }

    private void initTab() {
//        userHomeTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        userHomeTab = (TabLayout) findViewById(R.id.user_home_tab);
        mViewPager = (ViewPager) findViewById(R.id.vp_content);
        userHomeTab.addTab(userHomeTab.newTab());
        userHomeTab.addTab(userHomeTab.newTab());
        userHomeTab.addTab(userHomeTab.newTab());
        mViewPager.setAdapter(new UserHomeFragmentAdapter(getSupportFragmentManager(), getIntent().getStringExtra(USER_ID)));
        mViewPager.setOffscreenPageLimit(2);
        userHomeTab.setupWithViewPager(mViewPager);
    }


    private void initView() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        description = (TextView) findViewById(R.id.user_description);
        location = (TextView) findViewById(R.id.user_location);
        reg_time = (TextView) findViewById(R.id.user_reg_time);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);//onCreateOptionsMenu需要此方法支持,因为此activity引用的theme是noactionbar
        mToolbar.setNavigationIcon(R.drawable.ic_action_close);
//        mToolbar.inflateMenu(R.menu.user_home_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.direct_message:
                        ToastUtil.showToast(getApplicationContext(), "发送私信");
                        break;
                    case R.id.attention_request:
                        ToastUtil.showToast(getApplicationContext(), "已发送关注请求");
                        addFriend();
                        break;
                    case R.id.attention_cancel:
                        new AlertDialog.Builder(UserHomeActivity.this)
                                .setMessage("取消关注")
                                .setNegativeButton(android.R.string.cancel,
                                        (dialog, which) -> dialog.dismiss())
                                .setPositiveButton(android.R.string.ok,
                                        (dialog, which) -> {
                                            cancelFriend();
                                            dialog.dismiss();
                                        })
                                .show();
                        break;
                    default:
                        ToastUtil.showToast(getApplicationContext(), "error happened");
                }
                return true;
            }
        });
        friendsCount = (TextView) findViewById(R.id.friends_count);
        followersCount = (TextView) findViewById(R.id.followers_count);
        statusesCount = (TextView) findViewById(R.id.statuses_count);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        profileBackgroundImage = (ImageView) findViewById(R.id.profile_background_image);
        mCollapsingToolbarLayout.setLayoutMode(CollapsingToolbarLayout.FOCUSABLES_TOUCH_MODE);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_home_menu, menu);//加载menu文件到布局
        attentionRequest = menu.findItem(R.id.attention_request);
        attentionCancel = menu.findItem(R.id.attention_cancel);
        return true;
    }

    /**
     * 请求关注
     */
    private void addFriend() {
        FriendShipApi friendShipApi = BaseRetrofit.retrofit(new SignInterceptor()).create(FriendShipApi.class);
        Call<UserInfo> friendAddCall = friendShipApi.create(getIntent().getStringExtra(USER_ID));
        friendAddCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.code() == 200) {
                    attentionCancel.setVisible(true);
                    attentionRequest.setVisible(false);
                } else if(response.code()==403){
                    try {
                        ToastUtil.showToast(getApplicationContext(), response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                ToastUtil.showToast(UserHomeActivity.this, "请求关注失败");
            }
        });
    }
    private void cancelFriend() {
        FriendShipApi friendShipApi = BaseRetrofit.retrofit(new SignInterceptor()).create(FriendShipApi.class);
        Call<UserInfo> friendAddCall = friendShipApi.create(getIntent().getStringExtra(USER_ID));
        friendAddCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.code() == 200) {
                    attentionCancel.setVisible(false);
                    attentionRequest.setVisible(true);
                } else {
                    ToastUtil.showToast(getApplicationContext(), response.message());

                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                ToastUtil.showToast(UserHomeActivity.this, "取消关注失败");
            }
        });
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        UserApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(UserApi.class);
        Call<UserInfo> call = api.getUsersShow(getIntent().getStringExtra(USER_ID));
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.code() == 200) {
                    UserInfo userInfo = response.body();
                    if (userInfo != null) {
                        if (!TextUtils.isEmpty(userInfo.getDescription())) {
                            description.setText(userInfo.getDescription());
                        } else {
                            description.setText(getString(R.string.user_description));
                        }
                        location.setText(userInfo.getLocation());
                        reg_time.setText(DateUtil.toYear(userInfo.getCreatedAt()));
                        friendsCount.setText(formatCount(userInfo.getFriendsCount()));
                        followersCount.setText(formatCount(userInfo.getFollowersCount()));
                        statusesCount.setText(formatCount(userInfo.getStatusesCount()));
                        Glide.with(UserHomeActivity.this)
                                .load(userInfo.getProfileImageUrlLarge())
                                .into(profileImage);
                        Glide.with(UserHomeActivity.this)
                                .load(userInfo.getProfileBackgroundImageUrl())
                                .into(profileBackgroundImage);
                        mCollapsingToolbarLayout.setTitle(userInfo.getScreenName());
                        if (userInfo.isFollowing()) {
                            attentionCancel.setVisible(true);
                            attentionRequest.setVisible(false);
                        }
                    } else {
                        ToastUtil.showToast(UserHomeActivity.this, "没有查询到");
                    }
                } else {
                    try {
                        LogHelper.e(response.code() + "测试" + response.message() + "哈哈" + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                t.printStackTrace();
                ToastUtil.showToast(UserHomeActivity.this, "数据请求失败");
                LogHelper.e("请求失败", t.getMessage());
            }
        });
    }

    private String formatCount(int count) {
//        if (count < 10000) {
//            return String.valueOf(count);
//        } else {
//            String str = String.valueOf(count);
//            return str.substring(0, str.length() - 4) + "w+";
//        }
        return String.valueOf(count);
    }
}