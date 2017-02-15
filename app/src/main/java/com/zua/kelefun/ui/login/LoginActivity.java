package com.zua.kelefun.ui.login;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.zua.kelefun.R;
import com.zua.kelefun.config.AccountInfo;
import com.zua.kelefun.config.AccountStore;
import com.zua.kelefun.config.AppConfig;
import com.zua.kelefun.config.OAuthConst;
import com.zua.kelefun.data.api.OAuthTokenApi;
import com.zua.kelefun.exception.OAuthException;
import com.zua.kelefun.http.NetRequestListener;
import com.zua.kelefun.http.Parameter;
import com.zua.kelefun.http.RetrofitBase;
import com.zua.kelefun.util.OAuthEncoder;
import com.zua.kelefun.util.TokenUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mUsernameView, mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_login);
        //
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mLoginButton = (Button) findViewById(R.id.login_button);
        //登录按钮点击监听
        mLoginButton.setOnClickListener(view -> attemptLogin());
    }

    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for username and password
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            Log.d("logactivty", "输入有误,取消登录");
        } else {
            hideKeyboard(this, mPasswordView);
            // Show a progress spinner, and kick off a background task to
            showProgress(true);
            // TODO: 2017/2/10  异步执行登录调用
            new UserLoginTask(username, password).execute();
        }
    }

    private void hideKeyboard(final Context context, final EditText input) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        String username, password;

        public UserLoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //xauth请求获取token
             //   getAccessToken(username, password);
                //获取用户信息
                //getUserInfo();
                //保存用户信息
               // saveAccount();
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_invalid_account_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
    private void saveAccount(){
        AccountStore store = new AccountStore(this);
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setUserId("king_lau");
        accountInfo.setTokenAndSecret("936966-ef417ad5cebe68affba48ca25b6821f5","e3f53ef3be7f70060bbfde5c3705f119");
        store.saveAccount(accountInfo);
    }
    public void getAccessToken(String userName, String password) throws Exception {

        ArrayMap<String,String> paramMap = new ArrayMap<>();
        paramMap.put(OAuthConst.X_AUTH_USERNAME, userName);
        paramMap.put(OAuthConst.X_AUTH_PASSWORD, password);
        paramMap.put(OAuthConst.X_AUTH_MODE, "client_auth");
        paramMap.put(OAuthConst.TIMESTAMP, TokenUtil.getTimestampInSeconds());
        paramMap.put(OAuthConst.NONCE, TokenUtil.getNonce());
        paramMap.put(OAuthConst.CONSUMER_KEY, AppConfig.CONSUMER_KEY);
        paramMap.put(OAuthConst.SIGN_METHOD, TokenUtil.getSignatureMethod());
        paramMap.put(OAuthConst.VERSION, TokenUtil.getVersion());
        // TODO: 2017/2/14 排序???
        paramMap.put(OAuthConst.SIGNATURE, getSignature(paramMap));
        OAuthTokenApi api = RetrofitBase.retrofit().create(OAuthTokenApi.class);
        Call<ResponseBody> xauthCall = api.getAccessToken(extractAuthHeader(paramMap));
        RetrofitBase.AddToEnqueue(xauthCall, this, true, new NetRequestListener() {
            @Override
            public void onRequestSuccess(int code, retrofit2.Response response) {
                retrofit2.Response<ResponseBody> resultResponse = response;
                if (null != resultResponse.body()) {
                    try {
                        Log.d("请求结果",resultResponse.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onRequestFail(int code, String reason) {
                Log.d("测试","onRequestFail: " + code + ", " + reason);
            }
        });
    }
    private static final String PREAMBLE = "OAuth ";
    private static final String PARAM_SEPARATOR = ", ";
    public String extractAuthHeader(Map<String, String> params) {
        // Map<String, String> parameters = request.getOauthParameters();
        StringBuilder header = new StringBuilder(params.size()* 20);
        header.append(PREAMBLE);
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (header.length() > PREAMBLE.length()) {
                header.append(PARAM_SEPARATOR);
            }
            header.append(String.format("%s=\"%s\"", param.getKey(),
                    OAuthEncoder.encode(param.getValue())));
        }

        return header.toString();
    }
    public String extractBaseString(ArrayMap paramMap ) {
        String verb = OAuthEncoder.encode("POST");
        String url = OAuthEncoder.encode(AppConfig.ACCESS_TOKEN_URL);
        String params = getSortedAndEncodedParams(paramMap);
        return String.format("%s&%s&%s", verb, url, params);
    }
    public String getSignature(ArrayMap paramMap){
        try {
//            Preconditions.checkNotNull(extractBaseString(paramMap),
//                    "Base string cant be null or empty string");
//            Preconditions.checkNotNull(apiSecret,
//                    "Api secret cant be null or empty string");
            String keyString = OAuthEncoder.encode(AppConfig.CONSUMER_SECRET) + '&';
//            if (tokenSecret != null) {
//                keyString += OAuthEncoder.encode(tokenSecret);
//            }
            return doSignature(extractBaseString(paramMap), keyString);
        } catch (Exception e) {
            throw new OAuthException("basetring", e);
        }
    }
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";
    private String doSignature(String toSign, String keyString) throws Exception {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8),
                HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return bytesToBase64String(bytes);
    }
    private String getSortedAndEncodedParams(Map<String, String> params) {
        List<Parameter> paramList = new ArrayList<Parameter>();
        for (String key : params.keySet()) {
            paramList.add(new Parameter(key, params.get(key)));
        }
        Collections.sort(paramList);
        return OAuthEncoder.encode(asFormUrlEncodedString(paramList));
    }
    public static String asFormUrlEncodedString(List<Parameter> params) {
        if (params == null || params.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Parameter param : params) {
            builder.append('&').append(param.asUrlEncodedPair());
        }
        return builder.toString().substring(1);
    }
    private String bytesToBase64String(byte[] bytes) {
        return Base64.encodeToString(bytes,Base64.URL_SAFE);
    }
    private void showProgress(final boolean show) {
        // TODO: 2017/2/9
        Log.d("logactivty", "progress开始");
    }
}

