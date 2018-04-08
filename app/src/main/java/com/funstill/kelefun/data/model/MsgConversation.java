package com.funstill.kelefun.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * 私信对话对象
 * @author liukaiyang
 * @date 2018/4/8 21:24
 */
public class MsgConversation {
    @SerializedName("dm")
    private DirectMessage directMessage;
    @SerializedName("otherid")
    private String otherId;
    @SerializedName("msg_num")
    private String msgNum;
    @SerializedName("new_conv")
    private boolean newConv;

    public DirectMessage getDirectMessage() {
        return directMessage;
    }

    public void setDirectMessage(DirectMessage directMessage) {
        this.directMessage = directMessage;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(String msgNum) {
        this.msgNum = msgNum;
    }

    public boolean isNewConv() {
        return newConv;
    }

    public void setNewConv(boolean newConv) {
        this.newConv = newConv;
    }
}
