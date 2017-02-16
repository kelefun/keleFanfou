package com.zua.kelefun.ui.login;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.zua.kelefun.R;
import com.zua.kelefun.data.service.OAuthTokenService;

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
               new OAuthTokenService().getAccessToken(username, password);
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

//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_invalid_account_password));
//                mPasswordView.requestFocus();
//            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        // TODO: 2017/2/9
        Log.d("logactivty", "progress开始");
    }
}

