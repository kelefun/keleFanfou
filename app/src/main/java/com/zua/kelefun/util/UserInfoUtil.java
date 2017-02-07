package com.zua.kelefun.util;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.zua.kelefun.config.Constants;


/**
 *
 *@author liukaiyang
 *@since 2017/2/7 10:35
 */
public class UserInfoUtil {

    private SharedPreferencesUtil spUtil;

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
        spUtil.save(mContext, Constants.SP_LOGIN_INFO, map);
    }

    /**
     * 读取用户 Id
     *
     * @return 返回用户 Id
     */
    public String readUserId() {
        return spUtil.read(mContext, Constants.SP_LOGIN_INFO, "user_id", "");
    }

    /**
     * 读取用户 refresh token
     *
     * @return 返回用户 refresh token
     */
    public String readRefreshToken() {
        return spUtil.read(mContext, Constants.SP_LOGIN_INFO, "refresh_token", "");
    }

    /**
     * 获取用户 access token
     *
     * @return 返回用户 access token
     */
    public String readAccessToken() {
        return spUtil.read(mContext, Constants.SP_LOGIN_INFO, "access_token", "");
    }

    public void removeInfo() {
        spUtil.removeAll(mContext, Constants.SP_LOGIN_INFO);
    }

}
