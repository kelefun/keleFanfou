package com.zua.kelefun.data.service;

import com.zua.kelefun.config.AppConfig;
import com.zua.kelefun.data.api.OAuthTokenApi;
import com.zua.kelefun.data.model.OAuthToken;
import com.zua.kelefun.http.BaseRetrofit;
import com.zua.kelefun.util.LogHelper;
import com.zua.kelefun.util.OAuthEncoder;
import com.zua.kelefun.util.OAuthUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author liukaiyang
 * @since 2017/2/16 9:18
 */
public class OAuthTokenService {

    public OAuthToken getAccessToken(String username, String password) {
        Retrofit retrofit = BaseRetrofit.retrofit(AppConfig.FAN_FOU_HOST, chain -> {
            Request original = chain.request();
//            HttpUrl url = original.url().newBuilder()
//                    .addQueryParameter("count", "5")
//                    .addQueryParameter("start", "0")
//                    .build();

            Request request = original.newBuilder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .addHeader("Authorization", OAuthUtil.getInstance().extractHeader(original, username, password))
                    .method(original.method(), original.body())
                    .url(original.url())
                    .build();

            return chain.proceed(request);
        });
        OAuthTokenApi api = retrofit.create(OAuthTokenApi.class);
        Call<ResponseBody> call = api.getAccessToken();
        try {
            //因需返回请求结果,所以用同步请求方法 execute()
            Response<ResponseBody> response = call.execute();
            return extract(response.body().string());
        } catch (Exception e) {
            //TODO handle errors
            e.printStackTrace();

        }

        return null;
    }

    private static final Pattern TOKEN_REGEX = Pattern
            .compile("oauth_token=([^&]+)");
    private static final Pattern SECRET_REGEX = Pattern
            .compile("oauth_token_secret=([^&]*)");

    public OAuthToken extract(String response) {
        String token = extract(response, TOKEN_REGEX);
        String secret = extract(response, SECRET_REGEX);
        return new OAuthToken(token, secret);
    }

    private String extract(String response, Pattern p) {
        Matcher matcher = p.matcher(response);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return OAuthEncoder.decode(matcher.group(1));
        } else {
            LogHelper.e("Response body is incorrect. Can't extract token and secret from this: '" + response + "'");
            return null;
        }
    }

}
