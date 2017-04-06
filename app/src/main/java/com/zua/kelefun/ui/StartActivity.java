package com.zua.kelefun.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zua.kelefun.config.AccountInfo;
import com.zua.kelefun.config.AccountStore;
import com.zua.kelefun.ui.login.LoginActivity;

public class StartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVisible(false);
        chooseActivity();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void chooseActivity() {
        if (isLogin()) {
            showHome(this);
        } else {
            showLogin(this);
        }
        finish();
    }
    //启动app时判断是否已登录
    private boolean isLogin(){
        AccountStore store = new AccountStore(this);
        AccountInfo accountInfo = store.readAccount();
        return accountInfo.isVerified();
    }
    //显示登录页面
    private void showLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    //显示主页
    private void showHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
