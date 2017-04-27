package com.zua.imageselector.config;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.zua.imageselector.activity.GalleryPickActivity;
import com.zua.imageselector.utils.FileUtils;


/**
 * GalleryPick 启动类 (单例)
 * Created by Yancy on 2016/1/27.
 */
public class GalleryPick {

    private final static String TAG = "GalleryPick";

    private static GalleryPick galleryPick;

    private SeclectorConfig galleryConfig;

    public static GalleryPick getInstance() {
        if (galleryPick == null) {
            galleryPick = new GalleryPick();
        }
        return galleryPick;
    }


    public void open(Activity mActivity) {
        if (galleryPick.galleryConfig == null) {
            Log.e(TAG, "请配置 GalleryConfig");
            return;
        }
        if (galleryPick.galleryConfig.getIHandlerCallBack() == null) {
            Log.e(TAG, "请配置 IHandlerCallBack");
            return;
        }
        if (TextUtils.isEmpty(galleryPick.galleryConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }

        FileUtils.createFile(galleryPick.galleryConfig.getFilePath());

        Intent intent = new Intent(mActivity, GalleryPickActivity.class);
        mActivity.startActivity(intent);
    }

    public void openCamera(Activity mActivity) {
        if (galleryPick.galleryConfig == null) {
            Log.e(TAG, "请配置 GalleryConfig");
            return;
        }
        if (galleryPick.galleryConfig.getIHandlerCallBack() == null) {
            Log.e(TAG, "请配置 IHandlerCallBack");
            return;
        }
        if (TextUtils.isEmpty(galleryPick.galleryConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }

        FileUtils.createFile(galleryPick.galleryConfig.getFilePath());

        Intent intent = new Intent(mActivity, GalleryPickActivity.class);
        intent.putExtra("isOpenCamera", true);
        mActivity.startActivity(intent);
    }


    public GalleryPick setGalleryConfig(SeclectorConfig galleryConfig) {
        this.galleryConfig = galleryConfig;
        return this;
    }

    public SeclectorConfig getGalleryConfig() {
        return galleryConfig;
    }

    public void clearHandlerCallBack() {
        galleryConfig.getBuilder().iHandlerCallBack(null).build();
    }

}