package com.zua.imageselector.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.bumptech.glide.Glide;
import com.zua.imageselector.R;
import com.zua.imageselector.bean.PhotoInfo;
import com.zua.imageselector.config.GalleryPick;
import com.zua.imageselector.config.SeclectorConfig;
import com.zua.imageselector.widget.GalleryImageView;

import java.util.List;

/**
 * Mini选择器 适配器
 * Created by Yancy on 2016/2/3.
 */
public class MiniPhotoAdapter extends RecyclerView.Adapter<MiniPhotoAdapter.ViewHolder> {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<PhotoInfo> photoInfoList;
    private final static String TAG = "MiniPhotoAdapter";

    private SeclectorConfig galleryConfig = GalleryPick.getInstance().getGalleryConfig();

    public MiniPhotoAdapter(Context context, List<PhotoInfo> photoInfoList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.photoInfoList = photoInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.gallery_mini_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext)
                .load( photoInfoList.get(position).path)
                .placeholder(R.mipmap.gallery_pick_photo)
                .centerCrop()
                .into(holder.ivPhotoImage);
    }

    @Override
    public int getItemCount() {
        return photoInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private GalleryImageView ivPhotoImage;
        private CheckBox chkPhotoSelector;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhotoImage = (GalleryImageView) itemView.findViewById(R.id.ivGalleryPhotoImage);
            chkPhotoSelector = (CheckBox) itemView.findViewById(R.id.chkGalleryPhotoSelector);
        }

    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }
}
/*
 *   ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 *     ┃　　　┃
 *     ┃　　　┃
 *     ┃　　　┗━━━┓
 *     ┃　　　　　　　┣┓
 *     ┃　　　　　　　┏┛
 *     ┗┓┓┏━┳┓┏┛
 *       ┃┫┫　┃┫┫
 *       ┗┻┛　┗┻┛
 *        神兽保佑
 *        代码无BUG!
 */