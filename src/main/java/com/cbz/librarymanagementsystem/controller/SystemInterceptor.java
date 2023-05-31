package com.cbz.librarymanagementsystem.controller;

import com.cbz.librarymanagementsystem.entity.User;
import com.cbz.librarymanagementsystem.utils.UserHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class SystemInterceptor implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;

    public SystemInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = request.getHeader("authorization");

        if(token == null || token.equals("")){
            //拦截
            return true;
        }

        //根据token查询redis缓存
        String jsonStr = redisTemplate.opsForValue().get("login:token:" + token);

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonStr, User.class);

        if (user == null) {
            return true;
        }

        UserHolder.saveUser(user);

        //刷新有效期
        redisTemplate.expire("login:token:" + token,1L, TimeUnit.DAYS);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
