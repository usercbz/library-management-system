package com.cbz.librarymanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.template.PasswdData;
import com.cbz.librarymanagementsystem.template.RetrievePassData;

import javax.servlet.http.HttpServletRequest;

public interface IUserService extends IService<User> {
    Result login(User user);

    Result queryUserIsNull(String name);

    Result registerUser(User user);

    Result updatePasswd(PasswdData passwdData);

    Result updateUsername(String newUsername, HttpServletRequest request);

    Result deleteUserByPasswd(PasswdData passwdData,HttpServletRequest  request);

    Result sendMailMessage(RetrievePassData retrievePassData);

    Result retrievePassword(RetrievePassData retrievePassData);

    Result changeEmail(Integer id, String newEmail);

}
