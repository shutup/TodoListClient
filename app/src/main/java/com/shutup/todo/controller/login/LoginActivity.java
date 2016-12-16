package com.shutup.todo.controller.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shutup.todo.BuildConfig;
import com.shutup.todo.R;
import com.shutup.todo.controller.base.BaseActivity;
import com.shutup.todo.controller.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.loginBtn)
    Button mLoginBtn;
    @InjectView(R.id.registerText)
    TextView mRegisterText;
    @InjectView(R.id.userPhone)
    TextInputEditText mUserPhone;
    @InjectView(R.id.userPhoneInputLayout)
    TextInputLayout mUserPhoneInputLayout;
    @InjectView(R.id.password)
    TextInputEditText mPassword;
    @InjectView(R.id.passwordInputLayout)
    TextInputLayout mPasswordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        initToolBar();
        initInputLayout();
    }

    private void initInputLayout() {
        mUserPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkUserPhone();

            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkPassword();
            }
        });
    }

    private boolean checkUserPhone() {
        String userPhoneStr = mUserPhone.getEditableText().toString().trim();
        if (userPhoneStr.length() > 0 && userPhoneStr.length() == getResources().getInteger(R.integer.phoneNumLength)) {
            mUserPhoneInputLayout.setErrorEnabled(false);
            return true;
        }else {
            mUserPhoneInputLayout.setError("手机号码长度必须为11位!");
            mUserPhoneInputLayout.setErrorEnabled(true);
            return false;
        }
    }
    private boolean checkPassword() {
        String passwordStr = mPassword.getEditableText().toString().trim();
        if (passwordStr.length() >= 6 && passwordStr.length() <= getResources().getInteger(R.integer.maxPasswordLength)) {
            mPasswordInputLayout.setErrorEnabled(false);
            return true;
        }else {
            mPasswordInputLayout.setError("密码长度必须为6~20位!");
            mPasswordInputLayout.setErrorEnabled(true);
            return false;
        }
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        mToolbarTitle.setText(R.string.loginActivityTitle);
    }

    @OnClick({R.id.loginBtn, R.id.registerText})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.loginBtn:
                if (checkUserPhone()&& checkPassword()) {
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.registerText:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
