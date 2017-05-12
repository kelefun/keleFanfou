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
import com.funstill.kelefun.widget.ImagePreview;

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
        View view = inflater.inflate(R.layout.fragment_notice_mentions, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.line_recycler);
        mLayoutManager = new LinearLayoutManager(_mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.line_swipe_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorGreen, R.color.colorOrange, R.color.colorRed);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(true));

        mAdapter = new StatusAdapter(getActivity(),data);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((position, vh) -> {
            ToastUtil.showToast(_mActivity,"点击了卡片");
        });
        mAdapter.setOnPhotoClickListener((position, vh) -> {
            ImagePreview.startPreview(_mActivity,data.get(position).getPhoto().getLargeurl());
        });
        //初始化数据
        Map<String,String> map = new ArrayMap<>();
        map.put("count","20");
        getMentions(map);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                mInAtTop = mScrollTotal <= 0;
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
            mRefreshLayout.setRefreshing(true);
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


    /**
     * 请求home_timeline数据
     */
    private void getMentions(Map<String,String> param) {
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        Call<List<Status>> call = api.getMentions(param);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                LogHelper.d("请求响应code", String.valueOf(response.code()));
                if (response.code() == 200) {
                    List<Status> statusList = response.body();
                    if(statusList.size()>0){
//                        for(Status status:statusList){
//                            LogHelper.e(status.getId()+"###"+status.getText());
//                        }
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
                        ToastUtil.showToast(_mActivity,"Fun+ "+statusList.size());
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
}
