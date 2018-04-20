package com.funstill.lib.image.config;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.funstill.lib.image.utils.FileUtils;
import com.funstill.lib.image.view.ImageSelectorActivity;


public class ImageSelector {

    private final static String TAG = "ImageSelector";

    private static ImageSelector imageSelector;

    private SelectorConfig selectorConfig;

    public static ImageSelector getInstance() {
        if (imageSelector == null) {
            imageSelector = new ImageSelector();
        }
        return imageSelector;
    }


    public void open(Activity mActivity) {
        if (imageSelector.selectorConfig == null) {
            Log.e(TAG, "请配置 SelectorConfig");
            return;
        }
        if (imageSelector.selectorConfig.getIHandlerCallBack() == null) {
            Log.e(TAG, "请配置 IHandlerCallBack");
            return;
        }
        if (TextUtils.isEmpty(imageSelector.selectorConfig.getProvider())) {
            Log.e(TAG, "请配置 Provider");
            return;
        }

        FileUtils.createFile(imageSelector.selectorConfig.getFilePath());

        Intent intent = new Intent(mActivity, ImageSelectorActivity.class);
        mActivity.startActivity(intent);
    }

//    public void openCamera(Activity mActivity) {
//        if (imageSelector.selectorConfig == null) {
//            Log.e(TAG, "请配置 GalleryConfig");
//            return;
//        }
//        if (imageSelector.selectorConfig.getIHandlerCallBack() == null) {
//            Log.e(TAG, "请配置 IHandlerCallBack");
//            return;
//        }
//        if (TextUtils.isEmpty(imageSelector.selectorConfig.getProvider())) {
//            Log.e(TAG, "请配置 Provider");
//            return;
//        }
//
//        FileUtils.createFile(imageSelector.selectorConfig.getFilePath());
//
//        Intent intent = new Intent(mActivity, ImageSelectorActivity.class);
//        intent.putExtra("isOpenCamera", true);
//        mActivity.startActivity(intent);
//    }


    public SelectorConfig getSelectorConfig() {
        return selectorConfig;
    }

    public ImageSelector setSelectorConfig(SelectorConfig selectorConfig) {
        this.selectorConfig = selectorConfig;
        return this;
    }

    public void clearHandlerCallBack() {
        selectorConfig.getBuilder().iHandlerCallBack(null).build();
    }

}