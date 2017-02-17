package com.zua.kelefun.util;

import android.util.Log;

import com.zua.kelefun.BuildConfig;

public class LogHelper {
    private static boolean debug = BuildConfig.DEBUG;

    private static String logTag = "KeleFunLog";

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        if (debug) {
            if (tag == null) {
                tag = logTag;
            }
            Log.i(tag, msg);
        }
    }

    public static void d(String msg) {
        d(null, msg);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, String ext) {
        if (debug) {
            if (tag == null) {
                tag = logTag;
            }
            if (ext == null) {
                Log.d(tag, msg);
            } else {
                Log.d(tag, msg + "----->" + ext);
            }
        }
    }
    public static void e( String msg) {
        e(null,msg);
    }
    public static void e(String tag, String msg) {
        if (debug) {
            if (tag == null) {
                tag = logTag;
            }
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String value, String msg) {
        if (debug) {
            if (tag == null) {
                tag = logTag;
            }
            Log.e(tag, value + "----->" + msg);
        }
    }

}

