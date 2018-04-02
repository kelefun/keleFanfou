package com.funstill.kelefun.data.api;


import com.funstill.kelefun.data.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *账户相关
 *@author liukaiyang
 *@since 2018/4/2 14:15
 */
public interface FriendShipApi {

    /**
     * 获取用户信息
     * @param
     * @return
     */
    @POST("/friendships/create.json?mode=lite")
    Call<UserInfo> create(@Query("user_id")String user_id);

    @POST("/friendships/destroy.json?mode=lite")
    Call<UserInfo> destroy(@Query("user_id")String user_id);
}
