package com.zua.kelefun.data.api;


import com.zua.kelefun.data.model.UserInfo;

import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 *账户相关
 *@author liukaiyang
 *@since 2017/2/7 18:15
 */
public interface AccountApi {

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @POST("/account/verify_credentials")
    Observable<UserInfo> getUserInfo(
            @Path("userId") String userId
        //    @Query("oauth_token") String oauthToken
    );
}
