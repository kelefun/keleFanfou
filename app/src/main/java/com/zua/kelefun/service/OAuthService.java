package com.zua.kelefun.service;

import com.zua.kelefun.config.AppConfig;
import com.zua.kelefun.config.OAuthConst;
import com.zua.kelefun.data.model.OAuthToken;
import com.zua.kelefun.http.OAuthRequest;
import com.zua.kelefun.http.Parameter;
import com.zua.kelefun.util.Base64;
import com.zua.kelefun.util.LogHelper;
import com.zua.kelefun.util.OAuthEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author liukaiyang
 * @since 2017/2/17 15:47
 */

public class OAuthService {
    private  final String PARAM_SEPARATOR = ", ";
    private  final String PREAMBLE = "OAuth ";
    private  final int ESTIMATED_PARAM_LENGTH = 20;
    private  final String EMPTY_STRING = "";
    private  final String CARRIAGE_RETURN = "\r\n";
    private  final String VERSION = "1.0";
    private  final String UTF8 = "UTF-8";
    private  final String HMAC_SHA1 = "HmacSHA1";
    private  final String METHOD = "HMAC-SHA1";
    private  final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";
    //
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
    public void addOAuthParams(OAuthRequest request, OAuthToken token) {
        //
        request.setOauthParameter(OAuthConst.TOKEN, token.getToken());


        request.setOauthParameter(OAuthConst.TIMESTAMP,getTimestampInSeconds());
        request.setOauthParameter(OAuthConst.NONCE, getNonce());
        request.setOauthParameter(OAuthConst.CONSUMER_KEY,AppConfig.CONSUMER_KEY);
        request.setOauthParameter(OAuthConst.SIGN_METHOD, METHOD);
        request.setOauthParameter(OAuthConst.VERSION, VERSION);
        request.setOauthParameter(OAuthConst.SIGNATURE, getSignature(request, token));
    }
    //添加请求头 authorize 参数
    public void addXAuthParams(OAuthRequest request, String userName,
                                String password) {
        request.setOauthParameter(OAuthConst.X_AUTH_USERNAME, userName);
        request.setOauthParameter(OAuthConst.X_AUTH_PASSWORD, password);
        request.setOauthParameter(OAuthConst.X_AUTH_MODE, "client_auth");
        request.setOauthParameter(OAuthConst.TIMESTAMP, getTimestampInSeconds());
        request.setOauthParameter(OAuthConst.NONCE, getNonce());
        request.setOauthParameter(OAuthConst.CONSUMER_KEY, AppConfig.CONSUMER_KEY);
        request.setOauthParameter(OAuthConst.SIGN_METHOD, METHOD);
        request.setOauthParameter(OAuthConst.VERSION, VERSION);
        request.setOauthParameter(OAuthConst.SIGNATURE, getSignature(request, null));
    }


    //------------------------


    private String getSignature(OAuthRequest request, OAuthToken token) {
        String baseString = extractBaseString(request);
        String signature = getSignature(baseString, AppConfig.CONSUMER_SECRET,token == null ? null : token.getTokenSecret());
        LogHelper.d("base string is: " + baseString);
        return signature;
    }

    private String extractBaseString(OAuthRequest request) {
        String verb = OAuthEncoder.encode(request.getVerb());
        String url = OAuthEncoder.encode(request.getUrl());
        String params = getSortedAndEncodedParams(request);
        return String.format(AMPERSAND_SEPARATED_STRING, verb, url, params);
    }

    private String getSortedAndEncodedParams(OAuthRequest request) {
        List<Parameter> params = new ArrayList<>();
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

    private  String getTimestampInSeconds() {
        return String.valueOf(getTimeSecond());
    }
    private  Long getTimeSecond() {
        return System.currentTimeMillis() / 1000;
    }

    private  String getNonce() {
        final Random rand = new Random();
        return String.valueOf(getTimeSecond() + rand.nextInt());
    }
}
