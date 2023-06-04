package com.cbz.librarymanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.librarymanagementsystem.controller.Result;
import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.template.PasswdData;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

public interface IUserService extends IService<User> {
    Result login(User user);

    Result queryUserIsNull(String name);

    Result registerUser(User user);

    Result updatePasswd(PasswdData passwdData);

    Result updateUsername(String newUsername, HttpServletRequest request);

    Result deleteUserByPasswd(PasswdData passwdData,HttpServletRequest  request);
}
