package com.funstill.kelefun.http;

import com.funstill.kelefun.config.AppConfig;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofit {

    //默认url为 api.fanfou.com
    public static Retrofit retrofit() {
        return retrofit(AppConfig.API_HOST, null);
    }

    public static Retrofit retrofit(String baseUrl) {
        return retrofit(baseUrl, null);
    }

    public static Retrofit retrofit(Interceptor interceptor) {
        return retrofit(AppConfig.API_HOST, interceptor);
    }

    public static Retrofit retrofit(String baseUrl, Interceptor interceptor) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        // 打印请求日志
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//            okHttpBuilder.addInterceptor(logging);
        //添加拦截器
        if (interceptor != null) {
            okHttpBuilder.addInterceptor(interceptor);
        }

        OkHttpClient okHttpClient = okHttpBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

}