package com.funstill.kelefun.ui.notice.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.funstill.kelefun.event.TabSelectedEvent;
import com.funstill.kelefun.http.BaseRetrofit;
import com.funstill.kelefun.http.SignInterceptor;
import com.funstill.kelefun.ui.MainActivity;
import com.funstill.kelefun.util.LogHelper;
import com.funstill.kelefun.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.yokeyword.fragmentation.SupportFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author liukaiyang
 * @since 2017/5/12 9:11
 */

public class MentionsPagerFragment extends SupportFragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mRefreshLayout;
    private List<Status> data = new ArrayList<>();
    // 是否在加载中 ( 上拉加载更多 )
    private boolean isLoadingMore = false;
    private StatusAdapter mAdapter;

    private boolean mInAtTop = true;
    private int mScrollTotal;

    public static MentionsPagerFragment newInstance() {
        Bundle args = new Bundle();
        MentionsPagerFragment fragment = new MentionsPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_with_refresh, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.line_recycler);
        mLayoutManager = new LinearLayoutManager(_mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.no_toolbar_swipe_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorOrange, R.color.colorRed);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(true));

        mAdapter = new StatusAdapter(getActivity(),data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                mInAtTop = mScrollTotal <= 0;
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && (mLayoutManager.findLastVisibleItemPosition()+1== mLayoutManager.getItemCount())
                        &&!isLoadingMore) {
                    if(data.size()>0){
                        Map<String,String> loadMoreParam = new ArrayMap<>();
                        loadMoreParam.put("max_id",data.get(data.size()-1).getId());
                        loadMoreParam.put("count","20");
                        loadMoreMentions(loadMoreParam);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(true));
        Map<String,String> map = new ArrayMap<>();
        if(data.size()>0){
            map.put("since_id",data.get(0).getId());
        }else {
            map.put("count","20");
        }
        getMentions(map);
    }

    private void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    /**
     * 选择tab事件
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainActivity.FIRST) return;
        if (mInAtTop) {
            onRefresh();
        } else {
            scrollToTop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {//懒加载数据,为了防止veiwpager的预加载
        if(data.size()>0){
//            map.put("since_id",data.get(0).getId());
        }else {
            //初始化数据
            Map<String,String> map = new ArrayMap<>();
            map.put("count","20");
            getMentions(map);
        }
    }


    /**
     * 请求提到我的数据
     */
    private void getMentions(Map<String,String> param) {
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getMentions(param);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                LogHelper.d("请求响应code", String.valueOf(response.code()));
                if (response.code() == 200 ) {
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
                        mAdapter.notifyDataSetChanged();
                    }else{
                        if(data.size()>0){
                            ToastUtil.showToast(_mActivity,"没有更多了");
                        }else {
                            //暂时还没有数据
                            ToastUtil.showToast(_mActivity,"还没有相关数据");
                        }
                    }
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                mRefreshLayout.setRefreshing(false);
                t.printStackTrace();
                LogHelper.e("请求失败", t.getMessage());
            }
        });
    }
    private void loadMoreMentions(Map<String, String> param) {
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getMentions(param);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                if (response.code() == 200) {
                    List<Status> statusList = response.body();
                    if (statusList.size() > 0) {
                        data.addAll(statusList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showToast(_mActivity, "没有更多了");
                    }
                }
                isLoadingMore=false;
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                t.printStackTrace();
                LogHelper.e("请求失败", t.getMessage());
                isLoadingMore=false;
            }
        });
    }
}
