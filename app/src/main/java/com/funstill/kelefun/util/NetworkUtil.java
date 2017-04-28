package com.funstill.kelefun.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * 网络状态判断的工具类
 */
public class NetworkUtil {

    public static boolean hasNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        Network[] networks = connectivityManager.getAllNetworks();
        for (Network network : networks) {
            NetworkInfo networkInfo= connectivityManager.getNetworkInfo(network);
            if (networkInfo != null && networkInfo.isConnected()) return true;
        }
        return false;
    }

}
