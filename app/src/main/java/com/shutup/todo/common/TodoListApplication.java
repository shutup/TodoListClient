package com.shutup.todo.common;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import io.realm.Realm;

/**
 * Created by shutup on 2016/12/15.
 */

public class TodoListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Realm.init(getApplicationContext());
    }
}
