package com.funstill.kelefun.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;
import com.funstill.kelefun.data.model.Status;
import com.funstill.kelefun.listener.OnItemClickListener;
import com.funstill.kelefun.util.DateAgo;
import com.funstill.kelefun.util.LogHelper;

import net.wujingchao.android.view.SimpleTagImageView;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private Context mContext;
    private List<Status> data;
    private OnItemClickListener photoClickListener;
    private OnItemClickListener mClickListener;

    public StatusAdapter(Context mContext, List data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogHelper.e(viewType+"测试");
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_status, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (mClickListener != null) {
                mClickListener.onItemClick(position, holder);
            }
        });
        holder.photoView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (photoClickListener != null) {
                photoClickListener.onItemClick(position, holder);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        final View view = holder.mView;
        // 把每个图片视图设置不同的Transition名称, 防止在一个视图内有多个相同的名称, 在变换的时候造成混乱
        // Fragment支持多个View进行变换, 使用适配器时, 需要加以区分
        ViewCompat.setTransitionName(holder.screenNameView, String.valueOf(position) + "_screenNameView");
        ViewCompat.setTransitionName(holder.timeSourceView, String.valueOf(position) + "_timeSourceView");
        ViewCompat.setTransitionName(holder.statusView, String.valueOf(position) + "_statusView");
        ViewCompat.setTransitionName(holder.avatarView, String.valueOf(position) + "_avatarView");
        ViewCompat.setTransitionName(holder.photoView, String.valueOf(position) + "_photoView");

        if(data.size()!=0){
            Status status= data.get(position);
            holder.screenNameView.setText(status.getUser().getScreenName());
            holder.timeSourceView.setText(DateAgo.toAgo(status.getCreatedAt())+Html.fromHtml(status.getSource()).toString());
            holder.statusView.setText(status.getText());

            Glide.with(mContext)
                    .load(status.getUser().getProfileImageUrl())
//                    .placeholder(R.drawable.tab_item_bg)
                    .into(holder.avatarView);
            if(status.getPhoto() != null){
                holder.photoView.setVisibility(View.VISIBLE);
                if(status.getPhoto().getLargeurl().endsWith("gif")){
                    holder.photoView.setTagEnable(true);
                }
                Glide.with(mContext)
                        .load(status.getPhoto().getImageurl())
//                    .placeholder(R.drawable.tab_item_bg)
                        .into(holder.photoView);
            }else {
                holder.photoView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setOnPhotoClickListener(OnItemClickListener itemClickListener) {
        this.photoClickListener = itemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        public TextView screenNameView;
        public TextView timeSourceView;
        public TextView statusView;
        public ImageView avatarView;
        public SimpleTagImageView photoView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            screenNameView = (TextView) itemView.findViewById(R.id.screenNameView);
            timeSourceView = (TextView) itemView.findViewById(R.id.timeSourceView);
            statusView = (TextView) itemView.findViewById(R.id.statusView);
            avatarView = (ImageView) itemView.findViewById(R.id.avatarView);
            photoView = (SimpleTagImageView) itemView.findViewById(R.id.photoView);
        }
    }
}
