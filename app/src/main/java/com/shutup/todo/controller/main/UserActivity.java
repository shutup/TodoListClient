package com.shutup.todo.controller.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.shutup.todo.R;
import com.shutup.todo.controller.base.BaseActivity;
import com.shutup.todo.model.response.LoginUserResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;

public class UserActivity extends BaseActivity {

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.btnLoginName)
    Button mBtnLoginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.inject(this);
        initToolBar();

        initLoginName();
    }

    private void initLoginName() {
        LoginUserResponse loginUserResponse = Realm.getDefaultInstance().where(LoginUserResponse.class).findFirst();
        mBtnLoginName.setText(loginUserResponse.getToken());
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        mToolbarTitle.setText(R.string.userStr);
    }
}
