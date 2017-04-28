package com.funstill.kelefun.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author liukaiyang
 * @since 2017/2/6 17:52
 */

public class Status{

    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("id")
    private String id;
    @SerializedName("rawid")
    private int rawid;
    @SerializedName("text")
    private String text;
    @SerializedName("source")
    private String source;
    @SerializedName("truncated")
    private boolean truncated;
    @SerializedName("in_reply_to_status_id")
    private String inReplyToStatusId;
    @SerializedName("in_reply_to_user_id")
    private String inReplyToUserId;
    @SerializedName("in_reply_to_screen_name")
    private String inReplyToScreenName;
    @SerializedName("repost_status_id")
    private String repostStatusId;
    @SerializedName("repost_status")
    private Status repostStatus;
    @SerializedName("repost_user_id")
    private String repostUserId;
    @SerializedName("repost_screen_name")
    private String repostScreenName;
    @SerializedName("favorited")
    private boolean favorited;
    @SerializedName("user")
    private UserInfo user;
    @SerializedName("photo")
    private PhotoInfo photo;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRawid() {
        return rawid;
    }

    public void setRawid(int rawid) {
        this.rawid = rawid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public Status getRepostStatus() {
        return repostStatus;
    }

    public void setRepostStatus(Status repostStatus) {
        this.repostStatus = repostStatus;
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

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public PhotoInfo getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoInfo photo) {
        this.photo = photo;
    }
}
