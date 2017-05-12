package com.funstill.kelefun.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.funstill.kelefun.R;
import com.funstill.kelefun.config.AccountStore;
import com.funstill.kelefun.data.model.OAuthToken;
import com.funstill.kelefun.data.model.UserInfo;
import com.funstill.kelefun.data.service.OAuthTokenService;
import com.funstill.kelefun.util.LogHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameView, mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        } else {
            hideKeyboard(this, mPasswordView);
            // Show a progress spinner, and kick off a background task to
            showProgress(true);
            // TODO: 2017/2/10  异步执行登录
            new UserLoginTask(username, password).execute();
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param input
     */
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
                OAuthToken auth = new OAuthTokenService().getAccessToken(username, password);
                //获取用户信息
                // TODO: 2017/2/20  测试
                UserInfo u =  new UserInfo();
                u.setId("test");
                //保存用户信息
                saveAccount(auth,u);
            } catch (Exception e) {
                LogHelper.e("UserLoginTask异常",e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if (success) {
                showHome(LoginActivity.this);
                finish();
            } else {
                LogHelper.e("onPostExecute错误");
                // TODO: 2017/2/17 handle error
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        // TODO: 2017/2/9
        if(show){
            LogHelper.d("progress开始");
        }else {
            LogHelper.d("progress结束");
        }
    }
    private void  saveAccount(OAuthToken auth,UserInfo info){
        AccountStore store = new AccountStore(this);
        store.saveAccount(auth,info);
    }

    //显示主页
    private void showHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}

