package com.shutup.todo.common;

import com.shutup.todo.model.request.LoginUserRequest;
import com.shutup.todo.model.request.RegisterUserRequest;
import com.shutup.todo.model.response.RestInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shutup on 2016/12/14.
 */

public interface TodoListApi {
    @POST("/user/register")
    Call<RestInfo> registerUser(@Body RegisterUserRequest registerUserRequest);

    @POST("/user/login")
    Call<ResponseBody> loginUser(@Body LoginUserRequest loginUserRequest);
}
