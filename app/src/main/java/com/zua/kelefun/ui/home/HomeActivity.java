package com.zua.kelefun.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zua.kelefun.R;
import com.zua.kelefun.adapter.RecyclerViewAdapter;
import com.zua.kelefun.data.model.Status;
import com.zua.kelefun.data.service.StatusService;
import com.zua.kelefun.util.LogHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerview;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler();
    private List<Status> data = new ArrayList<>();
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, data);

    //  private RecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        //   new StatusService().getHomeLine();
        getData();
    }

    private void initView() {
          coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerview = (RecyclerView) findViewById(R.id.line_recycler);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.line_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorGreen, R.color.colorOrange, R.color.colorRed);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    /**
     * 获取测试数据
     */
    private void getData() {
        new StatusService().getHomeLine().enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                LogHelper.d("请求响应code", String.valueOf(response.code()));
                if (response.code() == 200) {
                    data.addAll(response.body());
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
//                adapter.notifyItemRemoved(adapter.getItemCount());
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
                LogHelper.e("请求失败", t.getMessage());
            }
        });
    }

}
