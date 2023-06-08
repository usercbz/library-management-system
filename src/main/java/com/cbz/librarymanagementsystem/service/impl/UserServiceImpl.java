package com.cbz.librarymanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.librarymanagementsystem.dto.Result;
import com.cbz.librarymanagementsystem.dto.UserDTO;
import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.mapper.UserMapper;
import com.cbz.librarymanagementsystem.service.IUserService;
import com.cbz.librarymanagementsystem.template.PasswdData;
import com.cbz.librarymanagementsystem.template.RetrievePassData;
import com.cbz.librarymanagementsystem.utils.BeanUtils;
import com.cbz.librarymanagementsystem.utils.DefaultMailMessage;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cbz.librarymanagementsystem.utils.RedisConst.*;
import static com.cbz.librarymanagementsystem.utils.SystemConst.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CollectBookServiceImpl collectBookService;

    @Autowired
    private JavaMailSender mailSender;

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


        if (usernameIsNotExist(name)) {
            return Result.succeed(null);
        }

        return Result.fail("用户名已存在");
    }

    private boolean usernameIsNotExist(String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", name);

        User user = getOne(wrapper);

        return user == null;
    }

    @Override
    public Result registerUser(User user) {
        if (!usernameIsNotExist(user.getUsername())) {
            return Result.fail("用户名已存在");
        }
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
        if (!collectBookService.getBaseMapper().deleteByUserId(id)) {
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

    @Override
    public Result sendMailMessage(RetrievePassData retrievePassData) {
        //校验信息是否真实
        String username = retrievePassData.getUsername();
        String email = retrievePassData.getEmail();
        if (checkUserData(username, email)) {
            //错误
            return Result.fail("用户名或邮箱有误");
        }
        //正确、发送验证码
        String code = createCode();
        //在redis保存验证码
        redisTemplate.opsForValue().set(RETRIEVE_CODE_KEY + username, code, RETRIEVE_CODE_TTL, TimeUnit.MINUTES);
        String info = SEND_CODE_INFO_PRE + code + SEND_CODE_INFO;
        //发送验证码邮件
        try {
            mailSender.send(new DefaultMailMessage(email, SEND_CODE_SUBJECT, info));
        } catch (MailException e) {
//            e.printStackTrace();
            return Result.fail(e.getMessage());
        }

        return Result.succeed(null);
    }

    @Override
    public Result retrievePassword(RetrievePassData retrievePassData) {
        //校验信息
        String username = retrievePassData.getUsername();
        String email = retrievePassData.getEmail();
        String code = retrievePassData.getCode();

        //校验用户信息
        if (checkUserData(username, email)) {
            //有误
            return Result.fail("用户名或邮箱有误");
        }
        //校验验证码
        String key = RETRIEVE_CODE_KEY + username;
        //1、查询redis缓存中的验证码
        String cacheCode = redisTemplate.opsForValue().get(key);
        //验证
        if (cacheCode == null || cacheCode.equals("") || !cacheCode.equals(code)) {
            return Result.fail("验证码错误");
        }
        //发送信息，重置密码
        //1、生成随机密码
        String password = createCode(6);
        //2、重置密码
        if (!baseMapper.updatePasswordByUsername(username, password)) {
            //失败
            return Result.fail("重置密码失败");
        }
        //成功、发送信息并删除redis中的缓存code
        try {
            String info = RESET_PASS_INFO_PRE + username + RESET_PASS_INFO + password;
            mailSender.send(new DefaultMailMessage(email, RESET_PASS_SUBJECT, info));
        } catch (MailException e) {
            return Result.fail(e.getMessage());
        }
        //删除
        redisTemplate.delete(key);

        return Result.succeed(null);
    }

    @Override
    public Result changeEmail(Integer id, String newEmail) {

        return baseMapper.updateEmailById(id, newEmail) ? Result.succeed(null) : Result.fail("修改失败");
    }

    private boolean checkUserData(String username, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("email", email);
        return getOne(queryWrapper) == null;
    }

    private boolean checkPassword(String password) {

        Integer id = UserHolder.getUser().getId();

        //校验原密码是否正确
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.eq("id", id).eq("password", password);

        User user = getOne(wrapper);

        return user == null;
    }

    public String createCode() {
        return createCode(CODE_LEN);
    }

    public String createCode(int len) {

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < len; i++) {
            code.append(STING_CODE_TEMP.charAt((int) (Math.random() * 36)));
        }

        return code.toString();
    }

}
