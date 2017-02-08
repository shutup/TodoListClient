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
import android.widget.Toast;

import com.google.gson.Gson;
import com.shutup.todo.BuildConfig;
import com.shutup.todo.R;
import com.shutup.todo.common.RetrofitSingleton;
import com.shutup.todo.common.TodoListApi;
import com.shutup.todo.controller.base.BaseActivity;
import com.shutup.todo.controller.main.MainActivity;
import com.shutup.todo.model.request.LoginUserRequest;
import com.shutup.todo.model.response.LoginUserResponse;
import com.shutup.todo.model.response.RestInfo;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private TodoListApi mTodoListApi;

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
        initApiInstance();
    }

    private void initApiInstance() {
        mTodoListApi = RetrofitSingleton.getApiInstance(TodoListApi.class);
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
                //just for test
//                intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
                if (checkUserPhone()&& checkPassword()) {
                    Call<ResponseBody> call= mTodoListApi.loginUser(new LoginUserRequest(mUserPhone.getEditableText().toString(),mPassword.getEditableText().toString()));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Gson gson = new Gson();
                            LoginUserResponse loginUserResponse =null;
                            if (response.isSuccessful()) {
                                try {
                                    loginUserResponse = gson.fromJson(response.body().string(),LoginUserResponse.class);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                realm.copyToRealm(loginUserResponse);
                                realm.commitTransaction();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                RestInfo info = null;
                                try {
                                    info = gson.fromJson(response.errorBody().string(),RestInfo.class);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (info!= null) {
                                    Toast.makeText(LoginActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(LoginActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            if (BuildConfig.DEBUG) Log.d("LoginActivity", "t:" + t);
                        }
                    });

                }
                break;
            case R.id.registerText:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
