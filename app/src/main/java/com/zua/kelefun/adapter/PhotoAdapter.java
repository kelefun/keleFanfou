package com.zua.kelefun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zua.kelefun.R;
import com.zua.kelefun.util.LogHelper;

import java.util.List;

/**
 * PhotoAdapter
 * Created by Yancy on 2016/9/29.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private List<String> result;

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
        Picasso.with(context)
                .load("file://"+result.get(position))
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
