package com.shutup.todo.model.response;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by shutup on 2016/12/18.
 */
public class LoginUserResponse extends RealmObject{
    @PrimaryKey
    private Long id;
    private String token;

    public LoginUserResponse(){

    }

    public LoginUserResponse(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEmpty(){
        return token.contentEquals("");
    }
}
