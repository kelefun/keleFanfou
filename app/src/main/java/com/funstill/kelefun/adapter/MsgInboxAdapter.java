package com.funstill.kelefun.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;
import com.funstill.kelefun.data.model.DirectMessage;
import com.funstill.kelefun.ui.other.UserHomeActivity;
import com.funstill.kelefun.util.DateUtil;
import com.funstill.kelefun.util.ToastUtil;

import java.util.List;

import static com.funstill.kelefun.config.KelefunConst.USER_ID;

public class MsgInboxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<DirectMessage> data;

    public MsgInboxAdapter(Context mContext, List<DirectMessage> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_msg_inbox, parent, false);
        final ItemViewHolder holder = new ItemViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            ToastUtil.showToast(mContext, "点击了卡片");
        });
        holder.msgInboxAvatar.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            goUserHome(position);
        });
        return holder;

    }

    private void goUserHome(int position) {
        Intent intent = new Intent(mContext, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(USER_ID, data.get(position).getSender().getId());
        mContext.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            if (data.size() != 0) {
                DirectMessage directMessage = data.get(position);
                itemHolder.msgInboxText.setText(directMessage.getText());
                itemHolder.msgInboxTime.setText(DateUtil.toAgo(directMessage.getCreatedAt()));
                Glide.with(mContext)
                        .load(directMessage.getSender().getProfileImageUrl())
                        .into(itemHolder.msgInboxAvatar);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView msgInboxTime;
        private TextView msgInboxText;
        private ImageView msgInboxAvatar;

        private ItemViewHolder(View view) {
            super(view);
            msgInboxTime = (TextView) itemView.findViewById(R.id.msg_inbox_time);
            msgInboxText = (TextView) itemView.findViewById(R.id.msg_inbox_text);
            msgInboxAvatar = (ImageView) itemView.findViewById(R.id.msg_inbox_avatar);
        }
    }

}
