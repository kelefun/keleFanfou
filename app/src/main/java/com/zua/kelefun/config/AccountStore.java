/**
 *
 */
package com.zua.kelefun.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.zua.kelefun.data.model.OAuthToken;

/**
 * 账号信息存储(SharedPreferences进行数据存储)
 */
public class AccountStore {
    private static final String STORE_NAME = "account_store";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_PROFILE_IMAGE = "profile_image";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_ACCESS_TOKEN_SECRET = "access_token_secret";
    private static final String KEY_LOGIN_NAME = "user_name";
    private static final String KEY_LOGIN_PASSWORD = "password";
    private Context mContext;
    private SharedPreferences mPreferences;

    public AccountStore(Context context) {
        this.mContext = context;
        this.mPreferences = mContext.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
    }

    public synchronized void saveAccessToken(String token, String tokenSecret) {
        if (token == null) {
            return;
        }
        Editor editor = mPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.putString(KEY_ACCESS_TOKEN_SECRET, tokenSecret);
        editor.apply();
    }

    public synchronized void saveAccessToken(OAuthToken token) {
        if (token == null) {
            return;
        }
        saveAccessToken(token.getToken(), token.getTokenSecret());
    }

    public synchronized OAuthToken readAccessToken() {
        String token = mPreferences.getString(KEY_ACCESS_TOKEN, null);
        String tokenSecret = mPreferences.getString(KEY_ACCESS_TOKEN_SECRET,
                null);
        if (TextUtils.isEmpty(tokenSecret)) {
            return null;
        }
        return new OAuthToken(token, tokenSecret);
    }

    public synchronized void clearAccessToken() {
        Editor editor = mPreferences.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_ACCESS_TOKEN_SECRET);
        editor.apply();
    }

    public synchronized void saveAccount(AccountInfo info) {
        if (info == null) {
            return;
        }
        Editor editor = mPreferences.edit();

        editor.putString(KEY_USER_ID, info.getUserId());
        editor.putString(KEY_SCREEN_NAME, info.getScreenName());
        editor.putString(KEY_PROFILE_IMAGE, info.getProfileImage());

        editor.putString(KEY_LOGIN_NAME, info.getUserName());
        editor.putString(KEY_LOGIN_PASSWORD, info.getPassword());

        editor.putString(KEY_ACCESS_TOKEN, info.getToken());
        editor.putString(KEY_ACCESS_TOKEN_SECRET, info.getTokenSecret());

        editor.commit();
    }
    public synchronized void saveAccount(
            String userId, String screenName,String profileImage,
            String userName, String password,String token,String tokenSecret) {
        Editor editor = mPreferences.edit();

        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_SCREEN_NAME, screenName);
        editor.putString(KEY_PROFILE_IMAGE, profileImage);
        editor.putString(KEY_LOGIN_NAME, userName);
        editor.putString(KEY_LOGIN_PASSWORD, password);

        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.putString(KEY_ACCESS_TOKEN_SECRET, tokenSecret);
        editor.commit();
    }
    public AccountInfo readAccount() {
        AccountInfo info = new AccountInfo();
        info.setUserId(mPreferences.getString(KEY_USER_ID, null));
        info.setScreenName(mPreferences.getString(KEY_SCREEN_NAME, null));
        info.setProfileImage(mPreferences.getString(KEY_PROFILE_IMAGE, null));
        info.setUserName(mPreferences.getString(KEY_LOGIN_NAME, null));
        info.setPassword(mPreferences.getString(KEY_LOGIN_PASSWORD, null));
        info.setTokenAndSecret(mPreferences.getString(KEY_ACCESS_TOKEN, null),
                mPreferences.getString(KEY_ACCESS_TOKEN_SECRET, null));

        return info;
    }

    public synchronized void clear() {
        Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();
    }




}
