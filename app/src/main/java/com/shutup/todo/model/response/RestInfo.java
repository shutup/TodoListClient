package com.shutup.todo.model.response;

/**
 * Created by shutup on 2016/12/16.
 */

public class RestInfo {
    private String msg;
    private boolean status;
    private int httpStatus;

    public RestInfo(String msg, boolean status, int httpStatus) {
        this.msg = msg;
        this.status = status;
        this.httpStatus = httpStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
