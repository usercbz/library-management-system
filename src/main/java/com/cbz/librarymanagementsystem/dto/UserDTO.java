package com.cbz.librarymanagementsystem.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    //id
    @TableId
    private Integer id;
    //用户名
    private String username;
    //权限 1、0
    private Integer permission;
}
