package com.zua.kelefun.util;

import com.zua.kelefun.config.AccountStore;
import com.zua.kelefun.config.AppConfig;
import com.zua.kelefun.config.OAuthConst;
import com.zua.kelefun.data.model.OAuthToken;
import com.zua.kelefun.http.OAuthRequest;
import com.zua.kelefun.http.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * okhttp拦截生成auth header
 *
 * @author liukaiyang
 * @since 2017/2/17 15:47
 */

public class OAuthUtil {
    private OAuthUtil(){}
    private static class SingletonHolder{
        private final static OAuthUtil instance=new OAuthUtil();
    }
    public static OAuthUtil getInstance(){
        return SingletonHolder.instance;
    }

    private final String PARAM_SEPARATOR = ", ";
    private final String PREAMBLE = "OAuth ";
    private final int ESTIMATED_PARAM_LENGTH = 20;
    private final String EMPTY_STRING = "";
    private final String CARRIAGE_RETURN = "\r\n";
    private final String VERSION = "1.0";
    private final String UTF8 = "UTF-8";
    private final String HMAC_SHA1 = "HmacSHA1";
    private final String METHOD = "HMAC-SHA1";
    private final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";


    /**
     * 普通接口请求
     *
     * @param request
     * @return
     */
    public String extractHeader(Request request) {
        //获取请求参数
        HttpUrl url = request.url();
        OAuthRequest oAuthRequest = new OAuthRequest();
        for (int i = 0; i < url.querySize(); i++) {
            oAuthRequest.setParameter(url.queryParameterName(i), url.queryParameterValue(i));
        }
        oAuthRequest.setVerb(request.method());
        oAuthRequest.setBaseUrl(extractBaseUrl(url));

        //oauth
        addOAuthParams(oAuthRequest);
        return extractHeader(oAuthRequest);
    }

    /**
     * 通过用户账号密码请求access_token
     *
     * @param request
     * @param username
     * @param password
     * @return
     */
    public String extractHeader(Request request, String username, String password) {
        HttpUrl url = request.url();
        OAuthRequest oAuthRequest = new OAuthRequest();
        for (int i = 0; i < url.querySize(); i++) {
            oAuthRequest.setParameter(url.queryParameterName(i), url.queryParameterValue(i));
        }
        oAuthRequest.setVerb(request.method());
        oAuthRequest.setBaseUrl(extractBaseUrl(url));
        //xauth
        addXAuthParams(oAuthRequest, username, password);
        return extractHeader(oAuthRequest);
    }

    private String extractHeader(OAuthRequest oAuthRequest) {
        List<Parameter> parameters = oAuthRequest.getOauthParameters();
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

    //获取baseurl,比如http://api.fanfou.com/account/verify_credentials.json
    private String extractBaseUrl(HttpUrl url) {
        StringBuilder result = new StringBuilder();
        result.append(url.scheme());
        result.append("://");
        result.append(url.host());

        int effectivePort = url.port();
        if (effectivePort != url.defaultPort(url.scheme())) {
            result.append(':');
            result.append(effectivePort);
        }
        for (int i = 0, size = url.pathSegments().size(); i < size; i++) {
            result.append('/');
            result.append(url.pathSegments().get(i));
        }
        LogHelper.d("baseUrl", result.toString());
        return result.toString();
    }

    private void addOAuthParams(OAuthRequest request) {
        OAuthToken token = AccountStore.readAccessToken();
        // TODO: 2017/3/27 如果 token == null
        request.setOauthParameter(OAuthConst.TOKEN, token.getToken());
        request.setOauthParameter(OAuthConst.TIMESTAMP, getTimestampInSeconds());
        request.setOauthParameter(OAuthConst.NONCE, getNonce());
        request.setOauthParameter(OAuthConst.CONSUMER_KEY, AppConfig.CONSUMER_KEY);
        request.setOauthParameter(OAuthConst.SIGN_METHOD, METHOD);
        request.setOauthParameter(OAuthConst.VERSION, VERSION);
        request.setOauthParameter(OAuthConst.SIGNATURE, getSignature(request, token));
    }

    //添加authorize 参数
    public void addXAuthParams(OAuthRequest request, String userName, String password) {
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
        LogHelper.d("base string is: " + baseString);
        try {
            String keyString = OAuthEncoder.encode(AppConfig.CONSUMER_SECRET) + '&';
            if (token != null) {
                keyString += OAuthEncoder.encode(token.getTokenSecret());
            }
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8),
                    HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(key);
            byte[] bytes = mac.doFinal(baseString.getBytes(UTF8));
            return bytesToBase64String(bytes).replace(CARRIAGE_RETURN, EMPTY_STRING);
        } catch (Exception e) {
            LogHelper.e("OAuthService", "signature异常", e.getMessage());
        }
        return "";
    }

    private String extractBaseString(OAuthRequest request) {
        String verb = OAuthEncoder.encode(request.getVerb());
        String url = OAuthEncoder.encode(request.getBaseUrl());
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


    private String bytesToBase64String(byte[] bytes) {
        return Base64.encodeBytes(bytes);
    }

    private String getTimestampInSeconds() {
        return String.valueOf(getTimeSecond());
    }

    private Long getTimeSecond() {
        return System.currentTimeMillis() / 1000;
    }

    private String getNonce() {
        final Random rand = new Random();
        return String.valueOf(getTimeSecond() + rand.nextInt());
    }
}
