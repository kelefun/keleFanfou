package com.zua.kelefun.data.api;


import com.zua.kelefun.data.model.OAuthToken;
import com.zua.kelefun.data.model.UserInfo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 *账户相关
 *@author liukaiyang
 *@since 2017/2/7 18:15
 */
public interface AccountApi {

    @GET("http://api2.fanfou.com/users/{userId}")
    Observable<UserInfo> getUserInfo(
            @Path("userId") String userId,
            @Query("oauth_token") String oauthToken
    );

    //xauth授权认证
    @GET("http://fanfou.com/oauth/request_token")
    Observable<OAuthToken> getAccessToken(
            @Query("x_auth_username") String username,
            @Query("x_auth_password") String password,
            @Query("x_auth_mode") String mode
    );
}
