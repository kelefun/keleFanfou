package com.zua.kelefun.data.service;

import com.zua.kelefun.data.api.StatusApi;
import com.zua.kelefun.http.BaseRetrofit;
import com.zua.kelefun.http.SignInterceptor;
import com.zua.kelefun.util.LogHelper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author liukaiyang
 * @since 2017/2/20 10:36
 */

public class StatusService {

    public void getHomeLine() {
        Retrofit retrofit = BaseRetrofit.retrofit(new SignInterceptor());
        StatusApi api = retrofit.create(StatusApi.class);
        Call<ResponseBody> call = api.getHomeTimeLine(2);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    LogHelper.d("status请求结果", response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                LogHelper.e("请求失败");
            }
        });
    }

}
