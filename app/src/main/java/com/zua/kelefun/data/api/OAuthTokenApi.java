package com.zua.kelefun.data.api;


import com.zua.kelefun.data.model.OAuthToken;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 *账户相关
 *@author liukaiyang
 *@since 2017/2/7 18:15
 */
public interface OAuthTokenApi {


    //xauth授权认证
    @POST("/oauth/access_token")
    Call<OAuthToken> getAccessToken(@Header("Authorization") String auth);
}
