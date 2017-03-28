package com.shutup.todo.model.response;

import java.util.Date;

/**
 * Created by shutup on 2017/3/6.
 */

public class DeleteTodoResponse {
    private Long  id;
    private Date createdAt ;
    private Date updatedAt ;
    private String todo;
    private boolean isFinish ;
    private boolean isDelete ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    protected DeleteTodoResponse(){}
}
