package com.funstill.kelefun.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * 私信
 *
 * @author liukaiyang
 * @date 2018/4/8 17:24
 */
public class DirectMessage {
    @SerializedName("id")
    private String id;
    @SerializedName("text")
    private String text;
    @SerializedName("sender_id")
    private String senderId;
    @SerializedName("recipient_id")
    private String recipientId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("sender_screen_name")
    private String senderScreenName;
    @SerializedName("recipient_screen_name")
    private String recipientScreenName;
    @SerializedName("sender")
    private UserInfo sender;
    @SerializedName("recipient")
    private UserInfo recipient;
    @SerializedName("in_reply_to")
    private DirectMessage inReplyTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSenderScreenName() {
        return senderScreenName;
    }

    public void setSenderScreenName(String senderScreenName) {
        this.senderScreenName = senderScreenName;
    }

    public String getRecipientScreenName() {
        return recipientScreenName;
    }

    public void setRecipientScreenName(String recipientScreenName) {
        this.recipientScreenName = recipientScreenName;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public UserInfo getRecipient() {
        return recipient;
    }

    public void setRecipient(UserInfo recipient) {
        this.recipient = recipient;
    }

    public DirectMessage getInReplyTo() {
        return inReplyTo;
    }

    public void setInReplyTo(DirectMessage inReplyTo) {
        this.inReplyTo = inReplyTo;
    }


}
