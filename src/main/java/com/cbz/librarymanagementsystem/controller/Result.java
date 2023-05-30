package com.cbz.librarymanagementsystem.controller;


import lombok.Data;

@Data
public class Result {

    private Integer code;
    private Object data;
    private String msg;

    public Result(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static Result succeed(Object data) {
        return new Result(1, data, null);
    }

    public static Result fail(String msg) {
        return new Result(0, null, msg);
    }
}
