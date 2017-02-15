package com.zua.kelefun.data.model;

import com.jakewharton.rxbinding.internal.Preconditions;

/**
 *@author liukaiyang
 *@since 2017/2/13 13:08
 */
public class OAuthToken {

    /**
     * access_token
     */
    private String token;
    /**
     * oauth 1.0: secret, oauth 2.0 refresh token
     */
    private String tokenSecret;
    public OAuthToken(String token, String tokenSecret) {
        Preconditions.checkNotNull(token, "Token can't be null");
        Preconditions.checkNotNull(tokenSecret, "Secret can't be null");
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OAuthToken{token=").append(token)
                .append(",secret=").append(tokenSecret)
                .append("}");

        return sb.toString();
    }
}
