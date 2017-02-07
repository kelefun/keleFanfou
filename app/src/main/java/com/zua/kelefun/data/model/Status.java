package com.zua.kelefun.data.model;

/**
 * @author liukaiyang
 * @since 2017/2/6 17:52
 */

public class Status extends BaseModel{


    //消息序列号(可用于排序
    private long rawId;
    //消息内容
    private String text; // html format text
    //
    private String simpleText; // plain text
    //消息来源
    private String source; // source
    //消息是否被截断
    private boolean truncated;
    //回复的消息id
    private String inReplyToStatusId;
    //回复的用户id
    private String inReplyToUserId;
    //回复的用户名称
    private String inReplyToScreenName;
    //转发的消息id
    private String repostStatusId;
    //转发的用户id
    private String repostUserId;
    //转发的用户名称
    private String repostScreenName;
    //消息的位置(格式可能是"北京 朝阳区"也可能是"234.333,47.9"
    private String location;
    private UserInfo userInfo;
    private PhotoInfo photoInfo;

    public long getRawId() {
        return rawId;
    }

    public void setRawId(long rawId) {
        this.rawId = rawId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSimpleText() {
        return simpleText;
    }

    public void setSimpleText(String simpleText) {
        this.simpleText = simpleText;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(String inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public String getInReplyToUserId() {
        return inReplyToUserId;
    }

    public void setInReplyToUserId(String inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public String getRepostStatusId() {
        return repostStatusId;
    }

    public void setRepostStatusId(String repostStatusId) {
        this.repostStatusId = repostStatusId;
    }

    public String getRepostUserId() {
        return repostUserId;
    }

    public void setRepostUserId(String repostUserId) {
        this.repostUserId = repostUserId;
    }

    public String getRepostScreenName() {
        return repostScreenName;
    }

    public void setRepostScreenName(String repostScreenName) {
        this.repostScreenName = repostScreenName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public PhotoInfo getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(PhotoInfo photoInfo) {
        this.photoInfo = photoInfo;
    }

}
