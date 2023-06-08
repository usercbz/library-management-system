package com.cbz.librarymanagementsystem.controller;

import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.service.impl.UserServiceImpl;
import com.cbz.librarymanagementsystem.template.PasswdData;
import com.cbz.librarymanagementsystem.template.RetrievePassData;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserServiceImpl userService;


    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        //登录功能实现
        return userService.login(user);
    }

    @GetMapping("/validate/{name}")
    public Result validateUser(@PathVariable("name") String name) {
        return userService.queryUserIsNull(name);
    }

    @PostMapping("/register")
    public Result registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping("/code")
    public Result sentCode() {
        return Result.succeed(userService.createCode());
    }

    @GetMapping
    public Result getList() {
        return Result.succeed(userService.list());
    }

    @GetMapping("/me")
    public Result getMe(){
        return Result.succeed(UserHolder.getUser());
    }

    @GetMapping("/resetName/{newUsername}")
    public Result resetUsername(@PathVariable String newUsername, HttpServletRequest request){
        return userService.updateUsername(newUsername,request);
    }

    @PostMapping("/updatePasswd")
    public Result updatePasswd(@RequestBody PasswdData passwdData){

        return userService.updatePasswd(passwdData);
    }

    @PostMapping("/unsubscribe")
    public Result unsubscribe(@RequestBody PasswdData passwdData,HttpServletRequest  request){
        return userService.deleteUserByPasswd(passwdData,request);
    }

    @PostMapping ("/mailCode")
    public Result sendMail(@RequestBody RetrievePassData retrievePassData){
        return userService.sendMailMessage(retrievePassData);
    }

    @PostMapping("/retrieve")
    public Result retrievePassword(@RequestBody RetrievePassData retrievePassData){
        return userService.retrievePassword(retrievePassData);
    }

    @GetMapping("/updateEmail/{newEmail}")
    public Result changeEmail(@PathVariable String newEmail){
        return userService.changeEmail(UserHolder.getUser().getId(),newEmail);
    }

}
