package com.funstill.kelefun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;
import com.funstill.kelefun.util.LogHelper;

import java.util.List;

/**
 * 用于发布状态时,选择的照片展示
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;

    private List<String> result;

    public void setResult(List<String> result) {
        this.result = result;
    }
    public PhotoAdapter(Context context, List<String> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        final PhotoAdapter.ViewHolder holder = new PhotoAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LogHelper.e(result.get(position));
        Glide.with(context)
                .load(result.get(position))
                .into(holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        }

    }


}
