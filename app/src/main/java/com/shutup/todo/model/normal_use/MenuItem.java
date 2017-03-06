package com.shutup.todo.model.normal_use;

import android.content.Intent;

/**
 * Created by shutup on 2016/12/14.
 */

public class MenuItem {
    private String menuName;
    private Intent mIntent;
    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public MenuItem(String menuName,Intent intent) {
        this.menuName = menuName;
        this.mIntent = intent;
    }
}
