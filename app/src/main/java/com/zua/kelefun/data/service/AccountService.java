package com.zua.kelefun.data.service;

import com.zua.kelefun.data.api.AccountApi;
import com.zua.kelefun.data.model.OAuthToken;
import com.zua.kelefun.data.model.UserInfo;
import com.zua.kelefun.http.BaseRetrofit;
import com.zua.kelefun.http.SignInterceptor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 *
 *@author liukaiyang
 *@since 2017/2/9 13:39
 */
public class AccountService{

    /**
     * @param auth 用户 Id
     */
    public UserInfo getUserInfo(OAuthToken auth){
        Retrofit retrofit = BaseRetrofit.retrofit(new SignInterceptor());
        AccountApi api = retrofit.create(AccountApi.class);
        Call<ResponseBody> call = api.verifyCred();
        try {
            Response<ResponseBody> response = call.execute();
     //       LogHelper.d(response.body().string());
        } catch (Exception e) {
            //TODO handle errors
            e.printStackTrace();
        }
        return null;
    }


}
