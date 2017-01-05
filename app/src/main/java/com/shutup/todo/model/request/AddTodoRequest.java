package com.shutup.todo.model.request;

/**
 * Created by shutup on 2016/12/18.
 */

public class AddTodoRequest {
    private String todo;

    public AddTodoRequest(String todo) {
        this.todo = todo;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}
