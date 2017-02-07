package com.zua.kelefun.data.model;

/**
 * Created by liukaiyang on 2017/2/6.
 */

public class OAuthToken {

    /**
     * access_token
     */
    private String token;
    /**
     * oauth 1.0: secret, oauth 2.0 refresh token
     */
    private String secret;
    /**
     * token 有效时间
     */
    private long expiresAt;
    private String userId;
    private String userName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OAuthToken{token=").append(token)
                .append(",secret=").append(secret)
                .append(",expiresAt=").append(expiresAt)
                .append(",userId=").append(userId).
                append(",userName=").append(userName)
                .append("}");

        return sb.toString();
    }
}
