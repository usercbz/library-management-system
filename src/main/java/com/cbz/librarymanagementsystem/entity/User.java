package com.cbz.librarymanagementsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    //id
    @TableId
    private Integer id;
    //用户名
    private String username;
    private String password;
    //权限 1、0
    private Integer permission;

    //icon

    //
}
