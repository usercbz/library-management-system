package com.cbz.librarymanagementsystem.controller;

import com.cbz.librarymanagementsystem.dto.UserDTO;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static com.cbz.librarymanagementsystem.utils.RedisConst.LOGIN_TOKEN_KEY_PRE;
import static com.cbz.librarymanagementsystem.utils.RedisConst.LOGIN_TOKEN_TTL;

public class SystemInterceptor implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;

    public SystemInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = request.getHeader("authorization");

        if (token == null || token.equals("")) {
            //拦截
            return true;
        }

        //根据token查询redis缓存
        String jsonUser = redisTemplate.opsForValue().get(LOGIN_TOKEN_KEY_PRE + token);

        if (jsonUser == null) {
            return true;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO user = objectMapper.readValue(jsonUser, UserDTO.class);

        if (user == null) {
            return true;
        }

        UserHolder.saveUser(user);

        //刷新有效期
        redisTemplate.expire(LOGIN_TOKEN_KEY_PRE + token, LOGIN_TOKEN_TTL, TimeUnit.DAYS);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
