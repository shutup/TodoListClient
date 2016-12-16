package com.shutup.todo.controller.main;

import com.shutup.todo.model.request.RegisterUserRequest;
import com.shutup.todo.model.response.RestInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shutup on 2016/12/14.
 */

public interface TodoListApi {
    @POST("/user/register")
    Call<RestInfo> registerUser(@Body RegisterUserRequest registerUserRequest);
}
