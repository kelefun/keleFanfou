package com.funstill.kelefun.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author liukaiyang
 * @since 2017/2/7 10:05
 */

public class PhotoInfo {
    @SerializedName("imageurl")
    private String imageurl;
    @SerializedName("thumburl")
    private String thumburl;
    @SerializedName("largeurl")
    private String largeurl;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getThumburl() {
        return thumburl;
    }

    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }

    public String getLargeurl() {
        return largeurl;
    }

    public void setLargeurl(String largeurl) {
        this.largeurl = largeurl;
    }
}
