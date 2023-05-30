package com.cbz.librarymanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.librarymanagementsystem.controller.Result;
import com.cbz.librarymanagementsystem.entity.User;

public interface IUserService extends IService<User> {
    Result login(User user);

    Result queryUserIsNull(String name);

    Result registerUser(User user);
}
