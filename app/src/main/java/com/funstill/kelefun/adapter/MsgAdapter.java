package com.funstill.kelefun.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funstill.kelefun.R;
import com.funstill.kelefun.data.model.DirectMessage;
import com.funstill.kelefun.ui.other.UserHomeActivity;
import com.funstill.kelefun.util.DateUtil;
import com.funstill.kelefun.util.ToastUtil;

import java.util.List;

import static com.funstill.kelefun.config.KelefunConst.USER_ID;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<DirectMessage> data;

    public MsgAdapter(Context mContext, List data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_msg, parent, false);
        final ItemViewHolder holder = new ItemViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            ToastUtil.showToast(mContext, "点击了卡片");
        });
        holder.msgAvatarLeft.setOnClickListener(v -> {
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
                itemHolder.msgTextLeft.setText(directMessage.getText());
                itemHolder.msgTextLeft.setText(DateUtil.toAgo(directMessage.getCreatedAt()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView msgTimeLeft;
        private TextView msgTextLeft;
        private ImageView msgAvatarLeft;
        private TextView msgTimeRight;
        private TextView msgTextRight;
        private ImageView msgAvatarRight;

        private ItemViewHolder(View view) {
            super(view);
            msgTimeLeft = (TextView) itemView.findViewById(R.id.msg_time_left);
            msgTextLeft = (TextView) itemView.findViewById(R.id.msg_text_left);
            msgAvatarLeft = (ImageView) itemView.findViewById(R.id.msg_avatar_left);
            msgTimeLeft = (TextView) itemView.findViewById(R.id.msg_time_right);
            msgTextLeft = (TextView) itemView.findViewById(R.id.msg_text_right);
            msgAvatarLeft = (ImageView) itemView.findViewById(R.id.msg_avatar_right);
        }
    }

}
