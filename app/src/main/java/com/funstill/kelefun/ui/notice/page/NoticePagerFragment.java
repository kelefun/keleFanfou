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
import com.funstill.kelefun.adapter.MsgConvListAdapter;
import com.funstill.kelefun.data.api.MsgApi;
import com.funstill.kelefun.data.model.MsgConversation;
import com.funstill.kelefun.event.TabSelectedEvent;
import com.funstill.kelefun.http.BaseRetrofit;
import com.funstill.kelefun.http.SignInterceptor;
import com.funstill.kelefun.ui.MainActivity;
import com.funstill.kelefun.util.LogHelper;

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
 * 好友请求等通知
 * @author liukaiyang
 * @since 2017/5/12 9:11
 */

public class NoticePagerFragment extends SupportFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mRefreshLayout;
    private List<MsgConversation> data = new ArrayList<>();
    // 是否在加载中 ( 上拉加载更多 )
    private boolean isLoadingMore = false;
    private MsgConvListAdapter mAdapter;

    private boolean mInAtTop = true;
    private int mScrollTotal;

    public static NoticePagerFragment newInstance() {
        Bundle args = new Bundle();
        NoticePagerFragment fragment = new NoticePagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_no_toolbar, container, false);
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

        mAdapter = new MsgConvListAdapter(getActivity(), data);
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
                        && (mLayoutManager.findLastVisibleItemPosition() + 1 == mLayoutManager.getItemCount())
                        && !isLoadingMore) {
                    if (data.size() > 0) {
                        Map<String, String> loadMoreParam = new ArrayMap<>();
                        loadMoreParam.put("count", "20");
                        loadMore(loadMoreParam);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(true));
        Map<String, String> map = new ArrayMap<>();
        map.put("count", "20");
        getMsgInbox(map);
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
        if (data.size() > 0) {
//            map.put("since_id",data.get(0).getId());
        } else {
            //初始化数据
            Map<String, String> map = new ArrayMap<>();
            map.put("count", "20");
            getMsgInbox(map);
        }
    }

    /**
     * 请求提到我的数据
     */
    private void getMsgInbox(Map<String, String> param) {
        MsgApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(MsgApi.class);
        Call<List<MsgConversation>> call = api.getConversationList(param);
        call.enqueue(new Callback<List<MsgConversation>>() {
            @Override
            public void onResponse(Call<List<MsgConversation>> call, Response<List<MsgConversation>> response) {
                LogHelper.d("请求响应code", String.valueOf(response.code()));
                if (response.code() == 200) {
                    List<MsgConversation> msgList = response.body();
                    data.clear();
                    data.addAll(msgList);
                    mAdapter.notifyDataSetChanged();
                }
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<MsgConversation>> call, Throwable t) {
                mRefreshLayout.setRefreshing(false);
                t.printStackTrace();
                LogHelper.e("请求失败", t.getMessage());
            }
        });
    }

    private void loadMore(Map<String, String> param) {
    }
}
