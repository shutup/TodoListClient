package com.shutup.todo.model.request;

/**
 * Created by shutup on 2016/12/16.
 */

public class RegisterUserRequest {
    private String username;
    private String password;

    public RegisterUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
