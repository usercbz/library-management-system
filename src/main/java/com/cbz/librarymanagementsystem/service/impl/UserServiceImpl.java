package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.librarymanagementsystem.controller.Result;
import com.cbz.librarymanagementsystem.dto.UserDTO;
import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.mapper.UserMapper;
import com.cbz.librarymanagementsystem.service.IUserService;
import com.cbz.librarymanagementsystem.utils.BeanUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
}
