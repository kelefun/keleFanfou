package com.zua.kelefun.ui.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;

import com.zua.kelefun.R;
import com.zua.kelefun.adapter.RecyclerViewAdapter;
import com.zua.kelefun.data.api.StatusApi;
import com.zua.kelefun.data.model.Status;
import com.zua.kelefun.http.BaseRetrofit;
import com.zua.kelefun.http.SignInterceptor;
import com.zua.kelefun.util.LogHelper;
import com.zua.kelefun.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Status> data = new ArrayList<>();
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, data);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        Map<String,String> map = new ArrayMap<>();
        map.put("page","1");
        getHomeLineStatus(map);
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.line_recycler);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);

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
                int firstItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                View view = mRecyclerView.getChildAt(firstItemPosition);
                RecyclerViewAdapter.StatusViewHolder viewHolder = (RecyclerViewAdapter.StatusViewHolder)mRecyclerView.getChildViewHolder(view);
                Map<String,String> map = new ArrayMap<>();
                map.put("since_id",viewHolder.statusIdView.getText().toString());
                LogHelper.d("消息id ",map.get("since_id"));
                getHomeLineStatus(map);
            }
        });
    }

    /**
     * 请求home_timeline数据
     */
    private void getHomeLineStatus(Map<String,String> param) {
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getHomeTimeLine(param);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                LogHelper.d("请求响应code", String.valueOf(response.code()));
                if (response.code() == 200) {
                    List<Status> statusList = response.body();
                    if(statusList.size()>0){
                        if(data.size()>0){ //让新增的数据在前面
                            List<Status> tempList  = new ArrayList<>();
                            tempList.addAll(data);
                            data.clear();
                            data.addAll(statusList);
                            data.addAll(tempList);
                        }else {
                            data.addAll(statusList);
                        }
                        adapter.notifyDataSetChanged();
                        ToastUtil.showToast(HomeActivity.this,"又盛了"+statusList.size()+"碗饭");
                    }else{
                        ToastUtil.showToast(HomeActivity.this,"没有更多了");
                    }
                    //测试数据
    //            Gson gson = new Gson();
    //            for(int i=0 ;i<data.size();i++){
    //                String jsonObject = gson.toJson(data.get(i));
    //                LogHelper.d("序号:"+i,"消息id:"+data.get(i).getId());
    //                LogHelper.d(jsonObject);
    //            }
                }

                swipeRefreshLayout.setRefreshing(false);
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
