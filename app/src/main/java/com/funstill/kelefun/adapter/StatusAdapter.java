package com.funstill.kelefun.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;
import com.funstill.kelefun.data.model.Status;
import com.funstill.kelefun.event.ActivitySpan;
import com.funstill.kelefun.event.TextLinkMovementMethod;
import com.funstill.kelefun.ui.userhome.UserHomeActivity;
import com.funstill.kelefun.ui.widget.ImagePreview;
import com.funstill.kelefun.util.DateUtil;
import com.funstill.kelefun.util.ToastUtil;

import net.wujingchao.android.view.SimpleTagImageView;

import java.util.List;

import static com.funstill.kelefun.config.KelefunConst.USER_ID;

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

            holder.photoView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                ImagePreview.startPreview(mContext, data.get(position).getPhoto().getLargeurl());
            });
            holder.photoView.setOnLongClickListener(v -> {
                int position = holder.getAdapterPosition();
                ToastUtil.showToast(mContext,"长按了卡片");
                return true;
            });
            holder.avatarView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                goUserHome(position);
            });
            holder.expandMenu.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                ToastUtil.showToast(mContext,"展开菜单");
            });
            holder.screenNameView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                goUserHome(position);
            });
            holder.itemView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                ToastUtil.showToast(mContext, "点击了卡片");
            });
            holder.itemView.setOnLongClickListener(v -> {
                int position = holder.getAdapterPosition();
                ToastUtil.showToast(mContext, "长按了卡片");
                return true;
            });
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_footer, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;

    }

    private void goUserHome(int position) {
        Intent intent = new Intent(mContext, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(USER_ID, data.get(position).getUser().getId());
        mContext.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            if (data.size() != 0) {
                Status status = data.get(position);
                itemHolder.screenNameView.setText(status.getUser().getScreenName());
                itemHolder.timeSourceView.setText(DateUtil.toAgo(status.getCreatedAt()) + Html.fromHtml(status.getSource()).toString());

                //处理文本点击跳转
                Spannable statusSpan = (Spannable) Html.fromHtml(status.getText());//格式化<a herf ,mobile等标签
                CharSequence text = statusSpan.toString();
                statusSpan.getSpans(0, text.length(), TextAppearanceSpan.class);
                URLSpan[] urls = statusSpan.getSpans(0, text.length(), URLSpan.class);
                SpannableStringBuilder activitySpan = new SpannableStringBuilder(statusSpan);
                activitySpan.clearSpans();
                for (URLSpan url : urls) {
                    ActivitySpan myURLSpan = new ActivitySpan(url.getURL());
                    activitySpan.setSpan(myURLSpan, statusSpan.getSpanStart(url), statusSpan.getSpanEnd(url), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                itemHolder.statusView.setText(activitySpan);
                itemHolder.statusView.setOnTouchListener(TextLinkMovementMethod.getInstance());//使标签可点击
                //图片加载
                Glide.with(mContext)
                        .load(status.getUser().getProfileImageUrl())
                        .into(itemHolder.avatarView);
                if (status.getPhoto() != null) {
                    itemHolder.photoView.setVisibility(View.VISIBLE);
                    if (status.getPhoto().getLargeurl().endsWith("gif")) {
                        itemHolder.photoView.setTagEnable(true);//动图标签
                    } else {
                        itemHolder.photoView.setTagEnable(false);
                    }
                    Glide.with(mContext)
                            .load(status.getPhoto().getImageurl())
                            .into(itemHolder.photoView);
                } else {
                    itemHolder.photoView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    /**
     * 获取onCreateViewHolder(ViewGroup parent, int viewType)viewType
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && getItemCount() > 5) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView screenNameView;
        private TextView timeSourceView;
        private TextView statusView;
        private ImageView avatarView;
        private ImageView expandMenu;
        private SimpleTagImageView photoView;

        private ItemViewHolder(View view) {
            super(view);
            screenNameView = (TextView) itemView.findViewById(R.id.screenNameView);
            screenNameView.setTag(1);
            timeSourceView = (TextView) itemView.findViewById(R.id.timeSourceView);
            statusView = (TextView) itemView.findViewById(R.id.statusView);
            expandMenu=(ImageView) itemView.findViewById(R.id.expand_more_menu);
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
