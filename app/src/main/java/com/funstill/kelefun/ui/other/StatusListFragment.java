package com.funstill.kelefun.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funstill.kelefun.R;
import com.funstill.kelefun.adapter.StatusAdapter;
import com.funstill.kelefun.data.api.StatusApi;
import com.funstill.kelefun.data.model.Status;
import com.funstill.kelefun.http.BaseRetrofit;
import com.funstill.kelefun.http.SignInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *列表
 *@author liukaiyang
 *@since 2017/10/18 16:56
 */
public class StatusListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    List<Status> data = new ArrayList<>();
    private static String tuserId;
    private StatusAdapter mAdapter;

    public static StatusListFragment newInstance(String userId) {
        StatusListFragment fragment = new StatusListFragment();
        tuserId=userId;
//        Bundle bundle = new Bundle();
//        bundle.putString(KEY, title);
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_no_toolbar, container, false);
        initView(view);
        return view;
    }
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.line_recycler);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.line_swipe_refresh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter =  new StatusAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);

        Map<String, String> map = new ArrayMap<>();
        map.put("id",tuserId);
        map.put("page","1");
        getHomeLineStatus(map);
    }

    private void getHomeLineStatus(Map<String, String> param) {
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getHomeTimeLine(param);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                if (response.code() == 200) {
                    List<Status> statusList = response.body();
                    if (statusList.size() > 0) {
                        if (data.size() > 0) { //让新增的数据在前面
                            List<Status> tempList = new ArrayList<>();
                            tempList.addAll(data);
                            data.clear();
                            data.addAll(statusList);
                            data.addAll(tempList);
                        } else {
                            data.addAll(statusList);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
//                        ToastUtil.showToast(this.c, "没有更多了");
                    }
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                mRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }
}
