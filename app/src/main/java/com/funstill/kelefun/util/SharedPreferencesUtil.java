package com.funstill.kelefun.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * SharedPreferences进行数据存储
 * @author liukaiyang
 * @since 2017/2/7 10:19
 */

public class SharedPreferencesUtil {
    private static SharedPreferencesUtil spUtil;

    private SharedPreferencesUtil() {
        //no instance
    }

    public static SharedPreferencesUtil getInstance() {
        if (spUtil == null) spUtil = new SharedPreferencesUtil();
        return spUtil;
    }

    /**
     * 保存数据
     *
     * @param context 上下文
     * @param name    SP 名字
     * @param key     保存键
     * @param value   保存值
     */
    public void save(Context context, String name, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 保存数据
     *
     * @param context 上下文
     * @param name    SP 名字
     * @param key     保存键
     * @param value   保存值
     */
    public void save(Context context, String name, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 保存数据
     *
     * @param context 上下文
     * @param name    SP 名字
     * @param key     保存键
     * @param value   保存值
     */
    public void save(Context context, String name, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 保存数据
     *
     * @param context 上下文
     * @param name    SP 名字
     * @param map     包含键值对的 Map
     */
    public void save(Context context, String name, Map<String, String> map) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.apply();
    }

    /**
     * 读取数据
     *
     * @param context      上下文
     * @param name         SP 名字
     * @param key          保存键
     * @param defaultValue 默认值
     * @return 返回读取到的值或默认值
     */
    public String read(Context context, String name, String key, @Nullable String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * 读取 int 值
     *
     * @param context      上下文
     * @param name         SP 名字
     * @param key          保存键
     * @param defaultValue 默认值
     * @return 返回读取到的值或默认值
     */
    public int read(Context context, String name, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 读取 long 值
     *
     * @param context      上下文
     * @param name         SP 名字
     * @param key          保存键
     * @param defaultValue 默认值
     * @return 返回读取到的值或默认值
     */
    public long read(Context context, String name, String key, long defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    public void remove(Context context, String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public void removeAll(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
