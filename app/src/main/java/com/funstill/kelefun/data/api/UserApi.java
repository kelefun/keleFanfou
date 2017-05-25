package com.funstill.kelefun.data.api;

import com.funstill.kelefun.data.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author liukaiyang
 * @since 2017/5/24 13:45
 */

public interface UserApi {
    @GET("/users/show.json")
    Call<UserInfo> getUsersShow(@Query("id") String id);
}
