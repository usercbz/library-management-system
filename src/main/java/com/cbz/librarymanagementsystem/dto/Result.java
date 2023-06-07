package com.cbz.librarymanagementsystem.dto;


import lombok.Data;

@Data
public class Result {
    //编码 1、成功  0、失败
    private Integer code;
    //返回数据
    private Object data;
    //附带信息
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
