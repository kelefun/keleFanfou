package com.funstill.kelefun.data.api;


import com.funstill.kelefun.data.model.Status;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * 消息相关
 *
 * @author liukaiyang
 * @since 2017/2/7 18:15
 */
public interface StatusApi {

    //获取用户主页timeline
    @GET("/statuses/home_timeline.json?mode=lite")
    Call<List<Status>> getHomeTimeLine(@QueryMap Map<String, String> paramMap);

    //获取随便看看
    @GET("/statuses/public_timeline.json?mode=lite")
    Call<List<Status>> getPublicTimeLine(@QueryMap Map<String, String> paramMap);

    //获取提到我的消息
    @GET("/statuses/mentions.json?mode=lite")
    Call<List<Status>> getMentions(@QueryMap Map<String, String> paramMap);

    /**
     * 发布消息(带照片)
     *
     * @param photo
     * @param status
     * @return
     * @link https://github.com/FanfouAPI/FanFouAPIDoc/wiki/photos.upload
     */
    @Multipart
    @POST("/photos/upload.json")
    Call<Status> uploadPhoto(@Part("status") RequestBody status,
                             @Part MultipartBody.Part photo);

    /**
     * 发布消息(只有文本)
     * @param paramMap
     * @return
     */
    @POST("/statuses/update.json")
    Call<Status> postStatus(@QueryMap Map<String, String> paramMap);
}
