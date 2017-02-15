package com.zua.kelefun.http;

import retrofit2.Response;

/**
 * Created by Alex9Xu@hotmail.com on 2016/7/20
 */
public interface NetRequestListener {
    void onRequestSuccess(int code, Response response);
    void onRequestFail(int code, String strToShow);
}
