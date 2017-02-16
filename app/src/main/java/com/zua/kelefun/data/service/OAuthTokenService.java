package com.zua.kelefun.data.service;

import com.zua.kelefun.config.AppConfig;
import com.zua.kelefun.config.OAuthConst;
import com.zua.kelefun.data.api.OAuthTokenApi;
import com.zua.kelefun.data.model.OAuthToken;
import com.zua.kelefun.http.BaseRetrofit;
import com.zua.kelefun.http.OAuthRequest;
import com.zua.kelefun.http.Parameter;
import com.zua.kelefun.util.Base64;
import com.zua.kelefun.util.OAuthEncoder;
import com.zua.kelefun.util.TokenUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 *
 *@author liukaiyang
 *@since 2017/2/16 9:18
 */
public class OAuthTokenService {

    public OAuthToken getAccessToken(String username,String password){
        OAuthRequest request = new OAuthRequest();
        addXAuthParams(request, username, password);
     //   appendSignature(request);
        Retrofit retrofit = BaseRetrofit.retrofit(AppConfig.FAN_FOU_HOST);
        OAuthTokenApi api = retrofit.create(OAuthTokenApi.class);

        Call<OAuthToken> call = api.getAccessToken(extractHeader(request));

        try {
            OAuthToken oAuthToken =  call.execute().body();
            System.out.println(oAuthToken.toString());
            return  oAuthToken;
        } catch (Exception e) {
            // handle errors
            e.printStackTrace();
        }
         return null;
    }
    private static final String PARAM_SEPARATOR = ", ";
    private static final String PREAMBLE = "OAuth ";
    public static final int ESTIMATED_PARAM_LENGTH = 20;
    public String extractHeader(OAuthRequest request) {
        List<Parameter> parameters = request.getOauthParameters();
        // Map<String, String> parameters = request.getOauthParameters();
        StringBuilder header = new StringBuilder(parameters.size() * ESTIMATED_PARAM_LENGTH);
        header.append(PREAMBLE);
        for (Parameter param : parameters) {
            if (header.length() > PREAMBLE.length()) {
                header.append(PARAM_SEPARATOR);
            }
            header.append(String.format("%s=\"%s\"", param.getName(),
                    OAuthEncoder.encode(param.getValue())));
        }

        return header.toString();
    }
    //添加请求头 authorize 参数
    private void addXAuthParams(OAuthRequest request, String userName,
                                String password) {
        request.setOauthParameter(OAuthConst.X_AUTH_USERNAME, userName);
        request.setOauthParameter(OAuthConst.X_AUTH_PASSWORD, password);
        request.setOauthParameter(OAuthConst.X_AUTH_MODE, "client_auth");
        request.setOauthParameter(OAuthConst.TIMESTAMP, TokenUtil.getTimestampInSeconds());
        request.setOauthParameter(OAuthConst.NONCE, TokenUtil.getNonce());
        request.setOauthParameter(OAuthConst.CONSUMER_KEY,AppConfig.CONSUMER_KEY);
        request.setOauthParameter(OAuthConst.SIGN_METHOD, TokenUtil.getSignatureMethod());
        request.setOauthParameter(OAuthConst.VERSION, TokenUtil.getVersion());
        request.setOauthParameter(OAuthConst.SIGNATURE, getSignature(request, null));
    }


    //------------------------


    private String getSignature(OAuthRequest request, OAuthToken token) {
        String baseString = extractBaseString(request);
        String signature = getSignature(baseString, AppConfig.CONSUMER_SECRET,token == null ? null : token.getTokenSecret());

        System.out.println("base string is: " + baseString);
        System.out.println("signature is: " + signature);
        return signature;
    }
    private static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";
    private String extractBaseString(OAuthRequest request) {
        String verb = OAuthEncoder.encode("POST");
        String url = OAuthEncoder.encode(AppConfig.FAN_FOU_HOST+"/oauth/access_token");
        String params = getSortedAndEncodedParams(request);
        return String.format(AMPERSAND_SEPARATED_STRING, verb, url, params);
    }

    private String getSortedAndEncodedParams(OAuthRequest request) {
        List<Parameter> params = new ArrayList<Parameter>();
//		if (request.isFormEncodedContent()) {
        params.addAll(request.getParameters());
//		}
        params.addAll(request.getOauthParameters());
        Collections.sort(params);
        return OAuthEncoder.encode(asFormUrlEncodedString(params));
    }
    private String asFormUrlEncodedString(List<Parameter> params) {
        if (params == null || params.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Parameter param : params) {
            builder.append('&').append(param.asUrlEncodedPair());
        }
        return builder.toString().substring(1);
    }


    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String METHOD = "HMAC-SHA1";

    /**
     * {@inheritDoc}
     */
    private String getSignature(String baseString, String apiSecret,String tokenSecret) {
        try {
//            Preconditions.checkEmptyString(baseString,
//                    "Base string cant be null or empty string");
//            Preconditions.checkEmptyString(apiSecret,
//                    "Api secret cant be null or empty string");
            String keyString = OAuthEncoder.encode(apiSecret) + '&';
            if (tokenSecret != null) {
                keyString += OAuthEncoder.encode(tokenSecret);
            }
            return doSign(baseString, keyString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String doSign(String toSign, String keyString) throws Exception {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8),
                HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return bytesToBase64String(bytes)
                .replace(CARRIAGE_RETURN, EMPTY_STRING);
    }

    private String bytesToBase64String(byte[] bytes) {
        return Base64.encodeBytes(bytes);
    }
}
