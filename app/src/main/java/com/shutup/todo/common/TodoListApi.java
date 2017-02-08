package com.shutup.todo.common;

import com.shutup.todo.model.request.AddTodoRequest;
import com.shutup.todo.model.request.LoginUserRequest;
import com.shutup.todo.model.request.RegisterUserRequest;
import com.shutup.todo.model.request.UpdateTodoRequest;
import com.shutup.todo.model.response.RestInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by shutup on 2016/12/14.
 */

public interface TodoListApi {
    /**
     * 注册用户
     * @param registerUserRequest
     * @return
     */
    @POST("/user/register")
    Call<RestInfo> registerUser(@Body RegisterUserRequest registerUserRequest);

    /**
     * 用户登录
     * @param loginUserRequest
     * @return
     */
    @POST("/user/login")
    Call<ResponseBody> loginUser(@Body LoginUserRequest loginUserRequest);

    @POST("/todo")
    Call<ResponseBody> createTodo(@Header("token") String token, @Body AddTodoRequest addTodoRequest);

    @PUT("/todo/{tid}")
    Call<ResponseBody> updateTodo(@Header("token") String token, @Path("tid") Long tid, @Body UpdateTodoRequest updateTodoRequest);
}
