package com.funstill.kelefun.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funstill.kelefun.R;

import java.util.ArrayList;
import java.util.List;

/**
 *列表
 *@author liukaiyang
 *@since 2017/10/18 16:56
 */
public class ListFragment extends Fragment {

    RecyclerView mRecyclerView;
    private static final String KEY = "key";
    private String title = "测试";

    List<String> mDatas = new ArrayList<>();
    private ItemAdapter mAdapter;

    public static ListFragment newInstance(String title) {
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        return view;
    }
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        for (int i = 0; i < 20; i++) {
            String s = String.format("我是第%d个" + title, i);
            mDatas.add(s);
        }

        mAdapter = new ItemAdapter();
        mRecyclerView.setAdapter(mAdapter);


    }


    public void scrollToFirst(boolean isSmooth) {
        if (mRecyclerView == null) {
            return;
        }
        if (isSmooth) {
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            mRecyclerView.scrollToPosition(0);
        }
    }
    class ItemAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_string, parent, false);
            final ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.textView.setText(mDatas.get(position));

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ItemViewHolder(View view) {
            super(view);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
