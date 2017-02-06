package com.zua.kelefun.ui.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.zua.kelefun.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void login(View v) {
        hideKeyboard();

        AutoCompleteTextView username =(AutoCompleteTextView) findViewById(R.id.username);
     //   String password = passwordWrapper.getEditText().getText().toString();

        // TODO: Checks

        // TODO: Login
    }
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
}

