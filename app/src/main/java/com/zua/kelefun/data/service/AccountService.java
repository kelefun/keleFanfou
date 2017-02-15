package com.zua.kelefun.data.service;

import com.zua.kelefun.data.api.AccountApi;
import com.zua.kelefun.data.model.UserInfo;

import retrofit2.Retrofit;
import rx.Observable;

/**
 *
 *@author liukaiyang
 *@since 2017/2/9 13:39
 */
public class AccountService{

    private static final String X_AUTH_MODE = "client_auth";

    private AccountApi accountApi;

    private AccountService() {
        Retrofit retrofit = new Retrofit.Builder()  //01:获取Retrofit对象
      //          .baseUrl("https://api.github.com/") //02采用链式结构绑定Base url
                .build();//03执行操作
        accountApi= retrofit.create(AccountApi.class);//04获取API接口的实现类的实例对象
    }

    public static AccountService getInstance() {
        return new AccountService();
    }

    /**
     * 获取用户资料
     *
     * @param userId 用户 Id
     */
    public Observable<UserInfo> getUserData(String userId) {
        return accountApi.getUserInfo(userId);
    }


}
