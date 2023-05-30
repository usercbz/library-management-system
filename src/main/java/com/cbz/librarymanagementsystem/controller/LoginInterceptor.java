package com.cbz.librarymanagementsystem.controller;

import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;

    public LoginInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取token
        String token = request.getHeader("authorization");

        if(token == null || token.equals("")){
            //拦截
            response.setStatus(401);
            return false;
        }

        //根据token查询redis缓存
        String jsonStr = redisTemplate.opsForValue().get("login:token:" + token);

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonStr, User.class);

        if (user == null) {
            return false;
        }

        UserHolder.saveUser(user);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
