package com.zua.kelefun.data.service;

import com.zua.kelefun.data.api.StatusApi;
import com.zua.kelefun.data.model.Status;
import com.zua.kelefun.http.BaseRetrofit;
import com.zua.kelefun.http.SignInterceptor;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * @author liukaiyang
 * @since 2017/2/20 10:36
 */

public class StatusService {

    public Call<List<Status>> getHomeLine(Map<String,String> map) {
        Retrofit retrofit = BaseRetrofit.retrofit(new SignInterceptor());
        StatusApi api = retrofit.create(StatusApi.class);
        Call<List<Status>> call = api.getHomeTimeLine(map);
        return call;
    }

}
