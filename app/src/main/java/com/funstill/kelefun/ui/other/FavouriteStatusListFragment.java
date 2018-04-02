package com.funstill.kelefun.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funstill.kelefun.R;
import com.funstill.kelefun.adapter.StatusAdapter;
import com.funstill.kelefun.config.KelefunConst;
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
public class FavouriteStatusListFragment extends Fragment{

    private RecyclerView mRecyclerView;
    List<Status> data = new ArrayList<>();
    private String tuserId;
    private StatusAdapter mAdapter;
    private boolean isLoadingMore = false;

    public static FavouriteStatusListFragment newInstance(String userId) {
        FavouriteStatusListFragment fragment = new FavouriteStatusListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KelefunConst.USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_no_toolbar_refresh, container, false);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.line_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StatusAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);

        if (getArguments() != null) {
            tuserId = getArguments().getString(KelefunConst.USER_ID);
        }
        Map<String, String> map = new ArrayMap<>();
        map.put("id", tuserId);
        map.put("page", "1");
        getFavourites(map);
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
                        loadMoreFavouriteStatus(loadMoreParam);
                    }
                }
            }
        });
    }
    private void getFavourites(Map<String, String> param) {
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getFavourites(param);
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
                        ToastUtil.showToast(getContext(), "没有收藏");
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

    private void loadMoreFavouriteStatus(Map<String, String> param) {
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getFavourites(param);
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
