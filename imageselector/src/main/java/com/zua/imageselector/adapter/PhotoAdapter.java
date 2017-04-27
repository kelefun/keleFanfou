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
import com.zua.imageselector.utils.ScreenUtils;
import com.zua.imageselector.widget.GalleryImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表中图片的适配器
 * Created by Yancy on 2016/1/27.
 */
public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<PhotoInfo> photoInfoList;                      // 本地照片数据
    private List<String> selectPhoto = new ArrayList<>();                   // 选择的图片数据
    private OnCallBack onCallBack;
    private final static String TAG = "PhotoAdapter";

    private SeclectorConfig galleryConfig = GalleryPick.getInstance().getGalleryConfig();

    private final static int HEAD = 0;    // 开启相机时需要显示的布局
    private final static int ITEM = 1;    // 照片布局

    public PhotoAdapter(Activity mActivity, Context mContext, List<PhotoInfo> photoInfoList) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.photoInfoList = photoInfoList;
        this.mActivity = mActivity;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new HeadHolder(mLayoutInflater.inflate(R.layout.gallery_item_camera, parent, false));
        }
        return new ViewHolder(mLayoutInflater.inflate(R.layout.gallery_item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // 设置 每个imageView 的大小
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = ScreenUtils.getScreenWidth(mContext) / 3;
        params.width = ScreenUtils.getScreenWidth(mContext) / 3;
        holder.itemView.setLayoutParams(params);

        if (getItemViewType(position) == HEAD) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (galleryConfig.getMaxSize() <= selectPhoto.size()) {        // 当选择图片达到上限时， 禁止继续添加
                        return;
                    }
                    onCallBack.OnClickCamera(selectPhoto);
                }
            });
            return;
        }

        final PhotoInfo photoInfo;
        if (galleryConfig.isShowCamera()) {
            photoInfo = photoInfoList.get(position - 1);
        } else {
            photoInfo = photoInfoList.get(position);
        }
        final ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(mContext)
                .load(photoInfo.path)
                .placeholder(R.mipmap.gallery_pick_photo)
                .centerCrop()
                .into(viewHolder.ivPhotoImage);

        if (selectPhoto.contains(photoInfo.path)) {
            viewHolder.chkPhotoSelector.setChecked(true);
            viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_checked);
            viewHolder.vPhotoMask.setVisibility(View.VISIBLE);
        } else {
            viewHolder.chkPhotoSelector.setChecked(false);
            viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_unchecked);
            viewHolder.vPhotoMask.setVisibility(View.GONE);
        }

        if (!galleryConfig.isMultiSelect()) {
            viewHolder.chkPhotoSelector.setVisibility(View.GONE);
            viewHolder.vPhotoMask.setVisibility(View.GONE);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!galleryConfig.isMultiSelect()) {
                    selectPhoto.clear();
                    selectPhoto.add(photoInfo.path);
                    onCallBack.OnClickPhoto(selectPhoto);
                    return;
                }

                if (selectPhoto.contains(photoInfo.path)) {
                    selectPhoto.remove(photoInfo.path);
                    viewHolder.chkPhotoSelector.setChecked(false);
                    viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_unchecked);
                    viewHolder.vPhotoMask.setVisibility(View.GONE);
                } else {
                    if (galleryConfig.getMaxSize() <= selectPhoto.size()) {        // 当选择图片达到上限时， 禁止继续添加
                        return;
                    }
                    selectPhoto.add(photoInfo.path);
                    viewHolder.chkPhotoSelector.setChecked(true);
                    viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_checked);
                    viewHolder.vPhotoMask.setVisibility(View.VISIBLE);
                }
                onCallBack.OnClickPhoto(selectPhoto);
            }
        });


    }


    /**
     * 照片的 Holder
     */
    private class ViewHolder extends RecyclerView.ViewHolder {
        private GalleryImageView ivPhotoImage;
        private View vPhotoMask;
        private CheckBox chkPhotoSelector;

        private ViewHolder(View itemView) {
            super(itemView);
            ivPhotoImage = (GalleryImageView) itemView.findViewById(R.id.ivGalleryPhotoImage);
            vPhotoMask = itemView.findViewById(R.id.vGalleryPhotoMask);
            chkPhotoSelector = (CheckBox) itemView.findViewById(R.id.chkGalleryPhotoSelector);
        }
    }

    /**
     * 相机按钮的 Holder
     */
    private class HeadHolder extends RecyclerView.ViewHolder {
        private HeadHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (galleryConfig.isShowCamera() && position == 0) {
            return HEAD;
        }
        return ITEM;
    }

    @Override
    public int getItemCount() {
        if (galleryConfig.isShowCamera())
            return photoInfoList.size() + 1;
        else
            return photoInfoList.size();
    }

    public interface OnCallBack {
        void OnClickPhoto(List<String> selectPhoto);

        void OnClickCamera(List<String> selectPhoto);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    /**
     * 传入已选的图片
     *
     * @param selectPhoto 已选的图片路径
     */
    public void setSelectPhoto(List<String> selectPhoto) {
        this.selectPhoto.addAll(selectPhoto);

//        for (String filePath : selectPhoto) {
//            PhotoInfo photoInfo = getPhotoByPath(filePath);
//            if (photoInfo != null) {
//                this.selectPhoto.add(photoInfo);
//            }
//        }
//        if (selectPhoto.size() > 0) {
//            notifyDataSetChanged();
//        }
    }

    //    /**
//     * 根据图片路径，获取图片 PhotoInfo 对象
//     *
//     * @param filePath 图片路径
//     * @return PhotoInfo 对象
//     */
//    private PhotoInfo getPhotoByPath(String filePath) {
//        if (photoInfoList != null && photoInfoList.size() > 0) {
//            for (PhotoInfo photoInfo : photoInfoList) {
//                if (photoInfo.path.equalsIgnoreCase(filePath)) {
//                    return photoInfo;
//                }
//            }
//        }
//        return null;
//    }


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