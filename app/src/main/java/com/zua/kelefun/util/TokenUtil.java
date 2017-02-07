package com.zua.kelefun.util;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.jakewharton.rxbinding.internal.Preconditions;
import com.zua.kelefun.exception.OAuthException;

import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 用于oauth授权工具类
 * @author liukaiyang
 * @since 2017/2/7 17:20
 */

public class TokenUtil {
    //    private Timer timer;
//    static class Timer {
//        private final Random rand = new Random();
//
//        Long getMilis() {
//            return System.currentTimeMillis();
//        }
//
//        Integer getRandomInteger() {
//            return rand.nextInt();
//        }
//    }
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String VERSION = "1.0";
    private static final String METHOD = "HMAC-SHA1";
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";
    /**
     * Returns the unix epoch timestamp in seconds
     *
     * @return timestamp
     */
    public static String getTimestampInSeconds() {
        return String.valueOf(getTimeSecond());
    }

    /**
     * Returns a nonce (unique value for each request)
     *
     * @return nonce
     */
    @NonNull
    public static String getNonce() {
        final Random rand = new Random();
        return String.valueOf(getTimeSecond() + rand.nextInt());
    }

    /**
     * oauth版本(auth1.0,oauth2
     * @return
     */
    public static String getVersion() {
        return VERSION;
    }

    public String getSignatureMethod(){
        return METHOD;
    }

    /**
     * Returns the signature
     *
     * @param baseString
     *            url-encoded string to sign
     * @param apiSecret
     *            api secret for your app
     * @param tokenSecret
     *            token secret (empty string for the request token step)
     *
     * @return signature
     */
    public String getSignature(String baseString, String apiSecret, String tokenSecret){
        try {
            Preconditions.checkNotNull(baseString,
                    "Base string cant be null or empty string");
            Preconditions.checkNotNull(apiSecret,
                    "Api secret cant be null or empty string");
            String keyString = OAuthEncoder.encode(apiSecret) + '&';
            if (tokenSecret != null) {
                keyString += OAuthEncoder.encode(tokenSecret);
            }
            return doSign(baseString, keyString);
        } catch (Exception e) {
            throw new OAuthException(baseString, e);
        }
    }
    private String doSign(String toSign, String keyString) throws Exception {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8),
                HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return bytesToBase64String(bytes);
    }

    private String bytesToBase64String(byte[] bytes) {
        return Base64.encodeToString(bytes,Base64.URL_SAFE);
    }

    @NonNull
    private static Long getTimeSecond() {
        return System.currentTimeMillis() / 1000;
    }
}
