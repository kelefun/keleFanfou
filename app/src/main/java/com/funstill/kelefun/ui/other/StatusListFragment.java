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
import com.funstill.kelefun.util.LogHelper;
import com.funstill.kelefun.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * g个人主页消息列表
 *
 * @author liukaiyang
 * @since 2017/10/18 16:56
 */
public class StatusListFragment extends Fragment{

    private RecyclerView mRecyclerView;
    List<Status> data = new ArrayList<>();
    private static String tuserId;
    private StatusAdapter mAdapter;
    private boolean isLoadingMore = false;

    public static StatusListFragment newInstance(String userId) {
        StatusListFragment fragment = new StatusListFragment();
        tuserId = userId;
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
        SwipeRefreshLayout mRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.line_swipe_refresh);
        mRefreshLayout.setEnabled(false);//禁用下拉刷新
        mRecyclerView = (RecyclerView) view.findViewById(R.id.line_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StatusAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);


        Map<String, String> map = new ArrayMap<>();
        map.put("id", tuserId);
        map.put("page", "1");
        getUserTimeLineStatus(map);
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
                    if (data.size() > 0) {
                        Map<String, String> loadMoreParam = new ArrayMap<>();
                        loadMoreParam.put("max_id", data.get(data.size() - 1).getId());
                        loadMoreParam.put("count", "20");
                        loadMoreParam.put("id", tuserId);
                        loadMoreHomeLineStatus(loadMoreParam);
                    }
                }
            }
        });
    }
    private void getUserTimeLineStatus(Map<String, String> param) {
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getUserTimeLine(param);
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
                        ToastUtil.showToast(getContext(), "没有更多了");
                    }
                }else if(response.code()==403){
                    ToastUtil.showToast(getContext(), "对方设置了隐私,需先请求关注");
                }
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadMoreHomeLineStatus(Map<String, String> param) {
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getUserTimeLine(param);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                if (response.code() == 200) {
                    List<Status> statusList = response.body();
                    if (statusList.size() > 0) {
                        data.addAll(statusList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showToast(getContext(), "没有更多了");
                    }
                }
                isLoadingMore = false;
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                t.printStackTrace();
                LogHelper.e("请求失败", t.getMessage());
                isLoadingMore = false;
            }
        });
    }

}
