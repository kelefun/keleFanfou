package com.zua.kelefun.http;

import com.zua.kelefun.util.LogHelper;
import com.zua.kelefun.util.OAuthUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * okhttp拦截器,添加auth认证请求头
 * @author liukaiyang
 * @since 2017/2/21 15:16
 */

public class SignInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
//            HttpUrl url = original.url().newBuilder()
//                    .addQueryParameter("count", "5")
//                    .addQueryParameter("start", "0")
//                    .build();

        Request request = original.newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("Authorization", OAuthUtil.getInstance().extractHeader(original))
                .method(original.method(), original.body())
                .url(original.url())
                .build();
        LogHelper.d("请求头",request.headers().toString());
        return chain.proceed(request);
    }
}
