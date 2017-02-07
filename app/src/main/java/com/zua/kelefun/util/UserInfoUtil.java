package com.zua.kelefun.util;

import android.content.Context;
import android.support.v4.util.ArrayMap;


/**
 *
 *@author liukaiyang
 *@since 2017/2/7 10:35
 */
public class UserInfoUtil {

    private SharedPreferencesUtil spUtil;
    private static final String SP_LOGIN_INFO ="loginInfo";
    private Context mContext;

    public UserInfoUtil(Context context) {
        mContext = context;
        spUtil = SharedPreferencesUtil.getInstance();
    }

    /**
     * 保存登陆信息
     *
     * @param access_token   AccessToken
     * @param douban_user_id UserId
     * @param refresh_token  RefreshToken
     */
    public void save(String access_token, String douban_user_id, String refresh_token) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("access_token", access_token);
        map.put("user_id", douban_user_id);
        map.put("refresh_token", refresh_token);
        spUtil.save(mContext, "", map);
    }

    /**
     * 读取用户 Id
     *
     * @return 返回用户 Id
     */
    public String readUserId() {
        return spUtil.read(mContext, SP_LOGIN_INFO, "user_id", "");
    }

    /**
     * 读取用户 refresh token
     *
     * @return 返回用户 refresh token
     */
    public String readRefreshToken() {
        return spUtil.read(mContext, SP_LOGIN_INFO, "refresh_token", "");
    }

    /**
     * 获取用户 access token
     *
     * @return 返回用户 access token
     */
    public String readAccessToken() {
        return spUtil.read(mContext, SP_LOGIN_INFO, "access_token", "");
    }

    public void removeInfo() {
        spUtil.removeAll(mContext, SP_LOGIN_INFO);
    }

}
