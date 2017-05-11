package com.funstill.library.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liukaiyang
 * @since 2017/4/24 19:43
 */

public class SelectorConfig {
    private IHandlerCallBack iHandlerCallBack;   // GalleryPick 生命周期接口

    private boolean multiSelect;        // 是否开启多选  默认 ： false
    private int maxSize;                // 配置开启多选时 最大可选择的图片数量。   默认：9
    private boolean showCamera;       // 是否开启相机 默认：true
    private String provider;            // 兼容android 7.0 设置
    private String filePath;            // 拍照以及截图后 存放的位置。    默认：/keleFun_img
    private ArrayList<String> pathList;      // 已选择照片的路径

    private boolean crop;                 // 是否开启裁剪   默认关闭
    private float aspectRatioX;             // 裁剪比         默认   1：1
    private float aspectRatioY;             // 裁剪比         默认   1：1
    private int maxWidth;                   // 最大的裁剪值   默认    500
    private int maxHeight;                  // 最大的裁剪值   默认    500

    private Builder builder;


    private SelectorConfig(Builder builder) {
        setBuilder(builder);
    }


    private void setBuilder(Builder builder) {
        this.iHandlerCallBack = builder.iHandlerCallBack;
        this.multiSelect = builder.multiSelect;
        this.maxSize = builder.maxSize;
        this.showCamera = builder.showCamera;
        this.pathList = builder.pathList;
        this.filePath = builder.filePath;
        this.crop = builder.crop;
        this.aspectRatioX = builder.aspectRatioX;
        this.aspectRatioY = builder.aspectRatioY;
        this.maxWidth = builder.maxWidth;
        this.maxHeight = builder.maxHeight;
        this.provider = builder.provider;
        this.builder = builder;
    }

    public static class Builder implements Serializable {

        private static SelectorConfig selectorConfig;

        private IHandlerCallBack iHandlerCallBack;

        private boolean multiSelect = false;
        private int maxSize = 9;
        private boolean showCamera = true;
        private String filePath = "/keleFun_img";

        private boolean crop = false;
        private float aspectRatioX = 1;
        private float aspectRatioY = 1;
        private int maxWidth = 500;
        private int maxHeight = 500;

        private String provider;

        private ArrayList<String> pathList = new ArrayList<>();

        private boolean isOpenCamera = false;

        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder crop(boolean crop) {
            this.crop = crop;
            return this;
        }

        public Builder crop(boolean crop, float aspectRatioX, float aspectRatioY, int maxWidth, int maxHeight) {
            this.crop = crop;
            this.aspectRatioX = aspectRatioX;
            this.aspectRatioY = aspectRatioY;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            return this;
        }



        public Builder iHandlerCallBack(IHandlerCallBack iHandlerCallBack) {
            this.iHandlerCallBack = iHandlerCallBack;
            return this;
        }


        public Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public Builder multiSelect(boolean multiSelect, int maxSize) {
            this.multiSelect = multiSelect;
            this.maxSize = maxSize;
            return this;
        }

        public Builder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder showCamera(boolean showCamera) {
            this.showCamera = showCamera;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder isOpenCamera(boolean isOpenCamera) {
            this.isOpenCamera = isOpenCamera;
            return this;
        }


        public Builder pathList(List<String> pathList) {
            this.pathList.clear();
            this.pathList.addAll(pathList);
            return this;
        }

        public SelectorConfig build() {
            if (selectorConfig == null) {
                selectorConfig = new SelectorConfig(this);
            } else {
                selectorConfig.setBuilder(this);
            }
            return selectorConfig;
        }

    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean showCamera() {
        return showCamera;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public String getFilePath() {
        return filePath;
    }

    public IHandlerCallBack getIHandlerCallBack() {
        return iHandlerCallBack;
    }

    public Builder getBuilder() {
        return builder;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public boolean crop() {
        return crop;
    }

    public float getAspectRatioX() {
        return aspectRatioX;
    }

    public float getAspectRatioY() {
        return aspectRatioY;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public String getProvider() {
        return provider;
    }
}
