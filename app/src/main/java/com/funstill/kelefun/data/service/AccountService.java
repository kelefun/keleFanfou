package com.funstill.kelefun.data.service;

import android.content.Context;

import com.funstill.kelefun.config.AccountStore;
import com.funstill.kelefun.data.api.AccountApi;
import com.funstill.kelefun.data.model.UserInfo;
import com.funstill.kelefun.http.BaseRetrofit;
import com.funstill.kelefun.http.SignInterceptor;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * @author liukaiyang
 * @since 2017/2/9 13:39
 */
public class AccountService {
    public void saveUserInfo(Context mContext) {
        Retrofit retrofit = BaseRetrofit.retrofit(new SignInterceptor());
        AccountApi api = retrofit.create(AccountApi.class);
        Call<UserInfo> call = api.verifyCred();
        try {
            UserInfo userInfo = call.execute().body();
            new AccountStore(mContext).saveAccount(userInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
