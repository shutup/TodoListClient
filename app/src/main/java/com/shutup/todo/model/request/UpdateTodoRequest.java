package com.shutup.todo.model.request;

import com.shutup.todo.model.persist.Todo;

/**
 * Created by shutup on 2017/2/8.
 */

public class UpdateTodoRequest {
    private String todo;
    private boolean delete;
    private boolean finish;

    public UpdateTodoRequest(){}

    public UpdateTodoRequest(Todo todo) {
        this.todo = todo.getTodo();
        this.finish = todo.isFinish();
        this.delete = todo.isDelete();
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
