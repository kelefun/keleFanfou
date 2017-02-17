package com.zua.kelefun.data.service;

import com.zua.kelefun.config.AppConfig;
import com.zua.kelefun.data.api.OAuthTokenApi;
import com.zua.kelefun.data.model.OAuthToken;
import com.zua.kelefun.http.BaseRetrofit;
import com.zua.kelefun.http.OAuthRequest;
import com.zua.kelefun.service.OAuthService;
import com.zua.kelefun.util.LogHelper;
import com.zua.kelefun.util.OAuthEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        OAuthRequest request = new OAuthRequest();
        request.setVerb("POST");
        request.setUrl(AppConfig.FAN_FOU_HOST+"/oauth/access_token");
        OAuthService oAuthService = new OAuthService();
        oAuthService.addXAuthParams(request, username, password);
        //   appendSignature(request);
        Retrofit retrofit = BaseRetrofit.retrofit(AppConfig.FAN_FOU_HOST);
        OAuthTokenApi api = retrofit.create(OAuthTokenApi.class);
        Call<ResponseBody> call = api.getAccessToken(oAuthService.extractHeader(request));
        try {
            Response<ResponseBody> response = call.execute();
            return extract(response.body().string());
        } catch (Exception e) {
            //TODO handle errors
            e.printStackTrace();
        }

//        call.enqueue(new Callback<ResponseBody>()
//        {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
//            {
//                try {
//                    System.out.println(response.body().string());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t)
//            {
//                t.printStackTrace();
//            }
//        });
        return null;
    }

    private static final Pattern TOKEN_REGEX = Pattern
            .compile("oauth_token=([^&]+)");
    private static final Pattern SECRET_REGEX = Pattern
            .compile("oauth_token_secret=([^&]*)");

    /**
     * {@inheritDoc}
     */
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
