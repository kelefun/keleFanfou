package com.funstill.kelefun.data.api;


import com.funstill.kelefun.data.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 *账户相关
 *@author liukaiyang
 *@since 2017/2/7 18:15
 */
public interface AccountApi {

    /**
     * 获取用户信息
     * @param
     * @return
     */
    @POST("/account/verify_credentials.json?mode=lite")
    Call<UserInfo> verifyCred();
}
