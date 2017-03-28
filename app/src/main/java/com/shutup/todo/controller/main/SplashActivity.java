package com.shutup.todo.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.shutup.todo.R;
import com.shutup.todo.common.Constants;
import com.shutup.todo.controller.base.BaseActivity;
import com.shutup.todo.controller.login.LoginActivity;
import com.shutup.todo.model.response.LoginUserResponse;

import io.realm.Realm;

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
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LoginUserResponse loginUserResponse = realm.where(LoginUserResponse.class).findFirst();
                if (loginUserResponse==null||loginUserResponse.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }
}
