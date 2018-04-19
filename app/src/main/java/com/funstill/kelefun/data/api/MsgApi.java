package com.funstill.kelefun.data.api;

import com.funstill.kelefun.data.model.DirectMessage;
import com.funstill.kelefun.data.model.MsgConversation;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * 私信
 */
public interface MsgApi {

    //显示20条收件箱中的私信
    @GET("/direct_messages/inbox.json?mode=lite")
    Call<List<DirectMessage>> getMsgInbox(@QueryMap Map<String, String> paramMap);
    //以对话的形式返回当前用户的私信列表
    @GET("/direct_messages/conversation_list.json?mode=lite")
    Call<List<MsgConversation>> getConversationList(@QueryMap Map<String, String> paramMap);
    //以对话的形式返回当前用户的私信
    @GET("/direct_messages/conversation.json?mode=lite")
    Call<List<DirectMessage>> getConversation(@QueryMap Map<String, String> paramMap);

    @FormUrlEncoded
    @POST("/direct_messages/new.json?mode=lite")
    Call<DirectMessage> sendMsg(@Field("user") String user,
                                @Field("text") String text, @Field("in_reply_to_id") String replyId);
}
