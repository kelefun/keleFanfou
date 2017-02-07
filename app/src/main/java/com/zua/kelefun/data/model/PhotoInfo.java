package com.zua.kelefun.data.model;

/**
 * @author liukaiyang
 * @since 2017/2/7 10:05
 */

public class PhotoInfo {
    private String photoImageUrl;
    private String photoThumbUrl;
    private String photoLargeUrl;

    public String getPhotoImageUrl() {
        return photoImageUrl;
    }

    public void setPhotoImageUrl(String photoImageUrl) {
        this.photoImageUrl = photoImageUrl;
    }

    public String getPhotoThumbUrl() {
        return photoThumbUrl;
    }

    public void setPhotoThumbUrl(String photoThumbUrl) {
        this.photoThumbUrl = photoThumbUrl;
    }

    public String getPhotoLargeUrl() {
        return photoLargeUrl;
    }

    public void setPhotoLargeUrl(String photoLargeUrl) {
        this.photoLargeUrl = photoLargeUrl;
    }
}
