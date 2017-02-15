/**
 *
 */
package com.zua.kelefun.config;

import com.zua.kelefun.data.model.OAuthToken;

/**
 * @author mcxiaoke
 * @version 1.0 2012-2-27 上午9:55:59
 */
public class AccountInfo {

    private String userId;
    private String screenName;
    private String profileImage;
    private String userName;
    private String password;
    private String token;
    private String tokenSecret;
    private OAuthToken accessToken;

    private static boolean isNotEmpty(String text) {
        return text != null && text.length() > 0;
    }

    public void setAccountInfo(String userId, String screenName) {
        this.userId = userId;
        this.screenName = screenName;
    }

    public void setAccountInfo(String userId, String screenName,
                               String profileImage) {
        this.userId = userId;
        this.screenName = screenName;
        this.profileImage = profileImage;
    }

    public void setTokenAndSecret(String token, String tokenSecret) {
        this.token = token;
        this.tokenSecret = tokenSecret;
        if (isNotEmpty(token) && isNotEmpty(tokenSecret)) {
            this.accessToken = new OAuthToken(token, tokenSecret);
        } else {
            this.accessToken = null;
        }
    }


    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public OAuthToken getAccessToken() {
        return new OAuthToken(token, tokenSecret);
    }

    public void setAccessToken(OAuthToken accessToken) {
        this.token = accessToken.getToken();
        this.tokenSecret = accessToken.getTokenSecret();
    }

    public boolean isVerified() {
        return !isEmpty(token) || !isEmpty(tokenSecret) || !isEmpty(userId);
    }

    private boolean isEmpty(String text) {
        return text == null || text.equals("");
    }

    public void clear() {
        this.userId = null;
        this.screenName = null;
        this.profileImage = null;
        this.userName = null;
        this.password = null;
        this.token = null;
        this.tokenSecret = null;
    }

    @Override
    public String toString() {
        return "AccountInfo [userId=" + userId + ", screenName=" + screenName
                + ", profileImage=" + profileImage + ", userName=" + userName
                + ", password=" + password + ", token=" + token
                + ", tokenSecret=" + tokenSecret + ", accessToken="
                + accessToken + "]";
    }

}
