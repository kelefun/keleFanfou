package com.zua.kelefun.data.api;


import com.zua.kelefun.data.model.Status;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 *消息相关
 *@author liukaiyang
 *@since 2017/2/7 18:15
 */
public interface StatusApi {

    //获取用户主页timeline
    @GET("/statuses/home_timeline.json")
    Call<List<Status>> getHomeTimeLine(@QueryMap Map<String,String> paramMap);
    //获取用户主页timeline
}
