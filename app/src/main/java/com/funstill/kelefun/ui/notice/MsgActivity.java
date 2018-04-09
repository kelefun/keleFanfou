package com.funstill.kelefun.ui.notice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;

import com.funstill.kelefun.R;
import com.funstill.kelefun.adapter.MsgAdapter;
import com.funstill.kelefun.data.api.MsgApi;
import com.funstill.kelefun.data.model.DirectMessage;
import com.funstill.kelefun.http.BaseRetrofit;
import com.funstill.kelefun.http.SignInterceptor;
import com.funstill.kelefun.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.funstill.kelefun.config.KelefunConst.USERNAME;
import static com.funstill.kelefun.config.KelefunConst.USER_ID;

/**
 * @author liukaiyang
 * @date 2018/4/9 9:38
 */
public class MsgActivity extends AppCompatActivity {
    private MsgAdapter mAdapter;
    private String tuserId;//对话用户的id
    private List<DirectMessage> data = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private boolean isLoadingMore = false;
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        initView();
        initData();
    }

    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getIntent().getStringExtra(USERNAME));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_close);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tuserId = getIntent().getStringExtra(USER_ID);
        mRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MsgAdapter(getBaseContext(), data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && (mLayoutManager.findLastVisibleItemPosition() + 1 == mLayoutManager.getItemCount())
                        && !isLoadingMore) {
                    isLoadingMore = true;
                    //加载更多
                }
            }
        });
    }

    private void initData() {
        Map<String, String> map = new ArrayMap<>();
        map.put("count", "15");
        map.put("id", tuserId);
        getMsg(map);
    }

    private void getMsg(Map<String, String> param) {
        MsgApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(MsgApi.class);
        Call<List<DirectMessage>> call = api.getConversation(param);
        call.enqueue(new Callback<List<DirectMessage>>() {
            @Override
            public void onResponse(Call<List<DirectMessage>> call, Response<List<DirectMessage>> response) {
                if (response.code() == 200) {
                    List<DirectMessage> msgList = response.body();
                    if (msgList.size() > 0) {
                        Collections.reverse(msgList);
                        data.clear();
                        data.addAll(msgList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showToast(getBaseContext(), "没有更多了");
                    }
                } else if (response.code() == 403) {
                    ToastUtil.showToast(getBaseContext(), "对方设置了隐私,需先请求关注");
                }
            }

            @Override
            public void onFailure(Call<List<DirectMessage>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
