package com.shutup.todo.controller.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;

import com.shutup.todo.R;
import com.shutup.todo.common.Constants;
import com.shutup.todo.controller.base.BaseActivity;
import com.shutup.todo.controller.login.LoginActivity;

public class SplashActivity extends BaseActivity implements Constants{

    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                judgeLogin();
                return false;
            }
        });
        mHandler.sendEmptyMessageDelayed(1,2000);
    }

    private void judgeLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String user_name = sharedPreferences.getString(USER_NAME,"");
        String user_token = sharedPreferences.getString(USER_TOKEN,"");
        if (user_name.equals("")||user_token.equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
