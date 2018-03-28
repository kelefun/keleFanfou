package com.funstill.kelefun.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;
import com.funstill.kelefun.adapter.UserHomeFragmentAdapter;
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

/**
 *用户主页
 *@author liukaiyang
 *@since 2017/10/18 16:56
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
    private ImageView attention;//关注
    private ImageView statusMention;//
    private ImageView directMessage;//私信
    private static final int ALPHA=32;//透明度
    public static final String USER_ID = "user_id";

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
    private void initTab(){
//        userHomeTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        userHomeTab = (TabLayout) findViewById(R.id.user_home_tab);
        mViewPager = (ViewPager) findViewById(R.id.vp_content);
        userHomeTab.addTab(userHomeTab.newTab());
        userHomeTab.addTab(userHomeTab.newTab());
        userHomeTab.addTab(userHomeTab.newTab());

        mViewPager.setAdapter( new UserHomeFragmentAdapter(getSupportFragmentManager(),getIntent().getStringExtra(USER_ID)));
        mViewPager.setOffscreenPageLimit(2);
        userHomeTab.setupWithViewPager(mViewPager);
    }


    private void initView() {
        mCollapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        description=(TextView)findViewById(R.id.user_description);
        location =(TextView)findViewById(R.id.user_location);
        reg_time=(TextView)findViewById(R.id.user_reg_time);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        friendsCount = (TextView) findViewById(R.id.friends_count);
        followersCount = (TextView) findViewById(R.id.followers_count);
        statusesCount = (TextView) findViewById(R.id.statuses_count);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        attention = (ImageView) findViewById(R.id.attention);
        statusMention = (ImageView) findViewById(R.id.status_mention);
        directMessage = (ImageView) findViewById(R.id.direct_message);
        profileBackgroundImage = (ImageView) findViewById(R.id.profile_background_image);
        mCollapsingToolbarLayout.setLayoutMode(CollapsingToolbarLayout.FOCUSABLES_TOUCH_MODE);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getUserInfo() {
        UserApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(UserApi.class);
        Call<UserInfo> call = api.getUsersShow(getIntent().getStringExtra(USER_ID));
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.code() == 200) {
                    UserInfo userInfo = response.body();
                    if (userInfo != null) {
                        if(!TextUtils.isEmpty(userInfo.getDescription())){
                            description.setText(userInfo.getDescription());
                        }else{
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
                        if(userInfo.isFollowing()){
                            attention.setImageResource(R.drawable.ic_action_attention_original);
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