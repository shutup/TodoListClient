package com.shutup.todo.model.request;

import com.shutup.todo.common.DateUtils;
import com.shutup.todo.model.persist.Todo;


/**
 * Created by shutup on 2016/12/18.
 */

public class AddTodoRequest {
    private String todo;
    private String createdAt;
    private String updatedAt;
    private boolean finish;
    private boolean delete;

    public AddTodoRequest(Todo todo) {
        this.todo = todo.getTodo();
        this.createdAt = DateUtils.formatDateForRequest(todo.getCreatedAt());
        this.updatedAt = DateUtils.formatDateForRequest(todo.getUpdatedAt());
        this.finish = todo.isFinish();
        this.delete = todo.isDelete();
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
