package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.librarymanagementsystem.controller.Result;
import com.cbz.librarymanagementsystem.dto.UserDTO;
import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.mapper.UserMapper;
import com.cbz.librarymanagementsystem.service.IUserService;
import com.cbz.librarymanagementsystem.template.PasswdData;
import com.cbz.librarymanagementsystem.utils.BeanUtils;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cbz.librarymanagementsystem.utils.RedisConst.LOGIN_TOKEN_KEY;
import static com.cbz.librarymanagementsystem.utils.RedisConst.LOGIN_TOKEN_TTL;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CollectBookServiceImpl collectBookService;


    @Override
    public Result login(User user) {
        //获取用户名，密码
        String username = user.getUsername();
        String password = user.getPassword();
        Integer permission = user.getPermission();

        //校验
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("password", password).eq("permission", permission);

        User queryUser = getOne(wrapper);

        if (queryUser == null) {
            return Result.fail("用户名或密码错误！");
        }

        UserDTO userDTO = BeanUtils.toBeanDTO(queryUser, UserDTO.class);

        //生成token
        String token = UUID.randomUUID().toString();

        //缓存
        String jsonUser = null;
        try {
            jsonUser = objectMapper.writeValueAsString(userDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        redisTemplate.opsForValue().set(LOGIN_TOKEN_KEY + token, jsonUser, LOGIN_TOKEN_TTL, TimeUnit.DAYS);

        return Result.succeed(token);
    }

    @Override
    public Result queryUserIsNull(String name) {

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", name);

        User user = getOne(wrapper);

        if (user == null) {
            return Result.succeed(null);
        }

        return Result.fail("用户名已存在");
    }

    @Override
    public Result registerUser(User user) {

        return save(user) ? Result.succeed(null) : Result.fail("注册失败！");
    }

    @Override
    public Result updatePasswd(PasswdData passwdData) {

        //校验密码
        if (checkPassword(passwdData.getOldPasswd())) {
            //原密码不正确
            return Result.fail("密码错误！");
        }
        //正确、修改密码

        return baseMapper.updatePasswordById(UserHolder.getUser().getId(), passwdData.getNewPasswd()) ? Result.succeed(null) : Result.fail("修改失败");
    }

    @Override
    public Result updateUsername(String newUsername, HttpServletRequest request) {

        boolean isReset = baseMapper.updateUsernameById(UserHolder.getUser().getId(), newUsername);

        if (!isReset) {
            return Result.fail("修改用户名失败");
        }
        //成功
        String token = request.getHeader("authorization");

        UserDTO user = UserHolder.getUser();
        UserHolder.removeUser();

        user.setUsername(newUsername);

        String jsonUser = null;
        try {
            jsonUser = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        redisTemplate.opsForValue().set(LOGIN_TOKEN_KEY + token, jsonUser, LOGIN_TOKEN_TTL, TimeUnit.DAYS);

        UserHolder.saveUser(user);
        return Result.succeed(null);
    }

    @Override
    public Result deleteUserByPasswd(PasswdData passwdData, HttpServletRequest request) {

        Integer id = UserHolder.getUser().getId();

        //校验密码
        if (checkPassword(passwdData.getOldPasswd())) {
            //密码不正确
            return Result.fail("密码错误！");
        }

        //删除用户的书籍
        if(!collectBookService.getBaseMapper().deleteByUserId(id)){
            return Result.fail("注销失败");
        }

        //删除用户
        if (removeById(id)) {

            //将redis的缓存删除
            String token = request.getHeader("authorization");

            redisTemplate.delete(LOGIN_TOKEN_KEY + token);
            return Result.succeed(null);
        }

        return Result.fail("注销失败");
    }

    private boolean checkPassword(String password) {

        Integer id = UserHolder.getUser().getId();

        //校验原密码是否正确
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.eq("id", id).eq("password", password);

        User user = getOne(wrapper);

        return user == null;
    }
}
