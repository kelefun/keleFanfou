package com.funstill.kelefun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;
import com.funstill.kelefun.data.model.Status;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;

    private List<Status> result;

    public void setResult(List<Status> result) {
        this.result = result;
    }
    public ImageAdapter(Context context, List<Status> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        final ImageAdapter.ViewHolder holder = new ImageAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status status=result.get(position);
        if(status!=null){
            if(status.getPhoto()!=null){
                Glide.with(context)
                        .load(status.getPhoto().getLargeurl())
                        .into(holder.image);
            }
            holder.imageText.setText(status.getText());
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView imageText;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            imageText = (TextView) itemView.findViewById(R.id.image_text);
        }

    }


}
