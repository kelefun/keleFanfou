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
import com.funstill.kelefun.config.AccountStore;
import com.funstill.kelefun.data.model.DirectMessage;
import com.funstill.kelefun.ui.userhome.UserHomeActivity;
import com.funstill.kelefun.util.DateUtil;
import com.funstill.kelefun.util.SharedPreferencesUtil;
import com.funstill.kelefun.util.ToastUtil;

import java.util.List;

import static com.funstill.kelefun.config.KelefunConst.USER_ID;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<DirectMessage> data;
private String  myUserId;
    public MsgAdapter(Context mContext, List data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_msg, parent, false);
        final ItemViewHolder holder = new ItemViewHolder(view);
        holder.itemView.setOnLongClickListener(v -> {
            int position = holder.getAdapterPosition();
            ToastUtil.showToast(mContext, "长按了卡片");
            return true;
        });
        holder.msgAvatarLeft.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            goUserHome(position);
        });
        holder.msgAvatarRight.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            goUserHome(position);
        });
        myUserId= SharedPreferencesUtil.getInstance().read(mContext,AccountStore.STORE_NAME,
                AccountStore.KEY_USER_ID,"");
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
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        if (data.size() > 0) {
            DirectMessage directMessage = data.get(position);
            if(directMessage.getSender().getId().equals(myUserId)){
                itemHolder.leftMsg.setVisibility(View.GONE);
                itemHolder.rightMsg.setVisibility(View.VISIBLE);
                itemHolder.msgTextRight.setText(directMessage.getText());
                itemHolder.msgTimeRight.setText(DateUtil.toAgo(directMessage.getCreatedAt()));
                Glide.with(mContext)
                        .load(directMessage.getSender().getProfileImageUrl())
                        .into(itemHolder.msgAvatarRight);
            }else {
                itemHolder.leftMsg.setVisibility(View.VISIBLE);
                itemHolder.rightMsg.setVisibility(View.GONE);
                itemHolder.msgTextLeft.setText(directMessage.getText());
                itemHolder.msgTimeLeft.setText(DateUtil.toAgo(directMessage.getCreatedAt()));
                Glide.with(mContext)
                        .load(directMessage.getSender().getProfileImageUrl())
                        .into(itemHolder.msgAvatarLeft);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private View leftMsg;
        private View rightMsg;
        private TextView msgTimeLeft;
        private TextView msgTextLeft;
        private ImageView msgAvatarLeft;
        private TextView msgTimeRight;
        private TextView msgTextRight;
        private ImageView msgAvatarRight;

        private ItemViewHolder(View view) {
            super(view);
            leftMsg = itemView.findViewById(R.id.left_msg);
            rightMsg = itemView.findViewById(R.id.right_msg);
            msgTimeLeft = (TextView) itemView.findViewById(R.id.msg_time_left);
            msgTextLeft = (TextView) itemView.findViewById(R.id.msg_text_left);
            msgAvatarLeft = (ImageView) itemView.findViewById(R.id.msg_avatar_left);
            msgTimeRight = (TextView) itemView.findViewById(R.id.msg_time_right);
            msgTextRight = (TextView) itemView.findViewById(R.id.msg_text_right);
            msgAvatarRight = (ImageView) itemView.findViewById(R.id.msg_avatar_right);
        }
    }

}
