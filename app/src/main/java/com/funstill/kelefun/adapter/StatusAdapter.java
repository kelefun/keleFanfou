package com.funstill.kelefun.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.funstill.kelefun.ui.other.UserHomeActivity;
import com.funstill.kelefun.util.DateAgo;
import com.funstill.kelefun.util.ToastUtil;
import com.funstill.kelefun.ui.widget.ImagePreview;

import net.wujingchao.android.view.SimpleTagImageView;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private List<Status> data;

    public StatusAdapter(Context mContext, List data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_status, parent, false);
            final ItemViewHolder holder = new ItemViewHolder(view);
            holder.itemView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                ToastUtil.showToast(mContext,"点击了卡片");
            });
            holder.userHomeClickArea.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(mContext, UserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtra(UserHomeActivity.USER_ID,data.get(position).getUser().getId());
                mContext.startActivity(intent);
            });
            holder.photoView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                ImagePreview.startPreview(mContext, data.get(position).getPhoto().getLargeurl());
            });
            holder.avatarView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                ImagePreview.startPreview(mContext,data.get(position).getUser().getProfileImageUrlLarge());
            });
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_footer, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            // 把每个图片视图设置不同的Transition名称, 防止在一个视图内有多个相同的名称, 在变换的时候造成混乱
            // Fragment支持多个View进行变换, 使用适配器时, 需要加以区分
            ViewCompat.setTransitionName(itemHolder.screenNameView, String.valueOf(position) + "_screenNameView");
            ViewCompat.setTransitionName(itemHolder.timeSourceView, String.valueOf(position) + "_timeSourceView");
            ViewCompat.setTransitionName(itemHolder.statusView, String.valueOf(position) + "_statusView");
            ViewCompat.setTransitionName(itemHolder.avatarView, String.valueOf(position) + "_avatarView");
            ViewCompat.setTransitionName(itemHolder.photoView, String.valueOf(position) + "_photoView");

            if(data.size()!=0){
                Status status= data.get(position);
                itemHolder.screenNameView.setText(status.getUser().getScreenName());
                itemHolder.timeSourceView.setText(DateAgo.toAgo(status.getCreatedAt())+Html.fromHtml(status.getSource()).toString());
                itemHolder.statusView.setText(Html.fromHtml(status.getText()));

                Glide.with(mContext)
                        .load(status.getUser().getProfileImageUrl())
//                    .placeholder(R.drawable.tab_item_bg)
                        .into(itemHolder.avatarView);
                if(status.getPhoto() != null){
                    itemHolder.photoView.setVisibility(View.VISIBLE);
                    if(status.getPhoto().getLargeurl().endsWith("gif")){
                        itemHolder.photoView.setTagEnable(true);//动图标签
                    }else {
                        itemHolder.photoView.setTagEnable(false);
                    }
                    Glide.with(mContext)
                            .load(status.getPhoto().getImageurl())
//                    .placeholder(R.drawable.tab_item_bg)
                            .into(itemHolder.photoView);
                }else {
                    itemHolder.photoView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        private View userHomeClickArea;
        public TextView screenNameView;
        public TextView timeSourceView;
        public TextView statusView;
        public ImageView avatarView;
        public SimpleTagImageView photoView;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            userHomeClickArea=itemView.findViewById(R.id.user_home_click_area);
            screenNameView = (TextView) itemView.findViewById(R.id.screenNameView);
            timeSourceView = (TextView) itemView.findViewById(R.id.timeSourceView);
            statusView = (TextView) itemView.findViewById(R.id.statusView);
            avatarView = (ImageView) itemView.findViewById(R.id.avatarView);
            photoView = (SimpleTagImageView) itemView.findViewById(R.id.photoView);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
}
