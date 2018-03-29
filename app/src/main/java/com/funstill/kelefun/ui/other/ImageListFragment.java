package com.funstill.kelefun.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funstill.kelefun.R;
import com.funstill.kelefun.adapter.ImageAdapter;
import com.funstill.kelefun.data.api.StatusApi;
import com.funstill.kelefun.data.model.Status;
import com.funstill.kelefun.http.BaseRetrofit;
import com.funstill.kelefun.http.SignInterceptor;
import com.funstill.kelefun.util.LogHelper;
import com.funstill.kelefun.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户图片消息列表
 *
 * @author liukaiyang
 * @since 2017/5/12 9:11
 */

public class ImageListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private int[] lastPositions;
    private int lastVisibleItemPosition;
    private List<Status> data = new ArrayList<>();
    // 是否在加载中 ( 上拉加载更多 )
    private boolean isLoadingMore = false;
    private ImageAdapter mAdapter;
    private static String tuserId;

    public static ImageListFragment newInstance(String userId) {
        ImageListFragment fragment = new ImageListFragment();
        tuserId = userId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_no_toolbar, container, false);
        initView(view);
        Map<String, String> map = new ArrayMap<>();
        map.put("page", "1");
        getStatusWithImage(map);
        return view;
    }

    private void initView(View view) {
        SwipeRefreshLayout mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.line_swipe_refresh);
        mRefreshLayout.setEnabled(false);//禁用下拉刷新
        mRecyclerView = (RecyclerView) view.findViewById(R.id.line_recycler);
        StaggeredGridLayoutManager   mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ImageAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastPositions = mLayoutManager.findLastVisibleItemPositions(lastPositions);
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(lastPositions==null||lastPositions.length==0){
                    return;
                }
                lastVisibleItemPosition = getMax(lastPositions);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && (lastVisibleItemPosition + 1 >= mLayoutManager.getItemCount())
                        && !isLoadingMore) {
                    if (data.size() > 0) {
                        Map<String, String> loadMoreParam = new ArrayMap<>();
                        loadMoreParam.put("max_id", data.get(data.size() - 1).getId());
                        loadMoreParam.put("count", "20");
                        loadMoreMentions(loadMoreParam);
                    }
                }
            }
        });
    }
    //获取int类型数组最大值
    public static int getMax(int[] arr)
    {
        int max = arr[0];
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]>max)
                max = arr[i];
        }
        return max;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }


    /**
     * 获取带图片的消息
     *
     * @param param
     */
    private void getStatusWithImage(Map<String, String> param) {
        param.put("id", tuserId);
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getUserTimeLineWithPhoto(param);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                LogHelper.d("请求响应code", String.valueOf(response.code()));
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
                        if (data.size() > 0) {
                            ToastUtil.showToast(getContext(), "没有更多了");
                        } else {
                            //暂时还没有数据
                            ToastUtil.showToast(getContext(), "还没有相关数据");
                        }
                    }
                }else if(response.code()==403){
                    ToastUtil.showToast(getContext(), "对方设置了隐私,需先请求关注");
                }
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                t.printStackTrace();
                LogHelper.e("请求失败", t.getMessage());
            }
        });
    }

    private void loadMoreMentions(Map<String, String> param) {
        param.put("id", tuserId);
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getUserTimeLineWithPhoto(param);
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
                }else if(response.code()==403){
                    ToastUtil.showToast(getContext(), "对方设置了隐私,需先请求关注");
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
