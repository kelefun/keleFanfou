package com.zua.kelefun.config;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/18
 */
public interface AppConfigInterface {

    /*====================Test Environment================= */
    String BASE_COM_URL = "http://www.weather.com.cn/";
    boolean isDebug = true;

    /*====================Real Environment==================== */
//    String BASE_COM_URL = "http://www.weather.com.cn/";
//    boolean isDebug = false;

    String VERSION = "1.1.0";
    String SERVER_CALL_VERSION = "1002";
    String TO_SERVER_VERSION = "1.0";
    String DEVICE_TYPE = "android";

    /**
     * Network Status Code
     */
    int RESULT_FAIL_UNKNOW = -1;

    /**
     * Get Data Url (Not Need Common part)
     */
    String GET_WEATHER = "/adat/sk/101010100.html";

}
