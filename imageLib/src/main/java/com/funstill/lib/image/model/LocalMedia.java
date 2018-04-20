package com.funstill.lib.image.model;

import java.io.Serializable;

/**
 * Created by dee on 2015/8/5.
 */
public class LocalMedia implements Serializable {
    //图片路径
    private String path;
    //图片名称
    private String name;
    //图片添加时间
    private long addDate;


    public LocalMedia(String path,String name, long addDate) {
        this.path = path;
        this.name = name;
        this.addDate = addDate;
    }

    public long getAddDate() {
        return addDate;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
