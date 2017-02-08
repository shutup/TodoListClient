package com.shutup.todo.model.response;

import java.util.Date;

/**
 * Created by shutup on 2016/12/18.
 */

public class AddTodoResponse{
    private Long  id;
    private String todo;
    private boolean isFinish;
    private boolean isDelete;

    public AddTodoResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

}
