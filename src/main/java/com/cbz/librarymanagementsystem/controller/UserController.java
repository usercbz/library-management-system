package com.cbz.librarymanagementsystem.controller;

import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.service.impl.UserServiceImpl;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.cbz.librarymanagementsystem.utils.SystemConst.CODE_LEN;
import static com.cbz.librarymanagementsystem.utils.SystemConst.STING_CODE_TEMP;

@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "http://127.0.0.1:5500", allowCredentials = "true")
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
        return Result.succeed(createCode());
    }

    @GetMapping
    public Result getList() {
        return Result.succeed(userService.list());
    }

    @GetMapping("/me")
    public Result getMe(){
        return Result.succeed(UserHolder.getUser());
    }


    private String createCode() {

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LEN; i++) {
            code.append(STING_CODE_TEMP.charAt((int) (Math.random() * 36)));
        }

        return code.toString();
    }
}
