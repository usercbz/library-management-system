package com.cbz.librarymanagementsystem.config;

import com.cbz.librarymanagementsystem.controller.LoginInterceptor;
import com.cbz.librarymanagementsystem.controller.SystemInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper jsonMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new SystemInterceptor(redisTemplate, jsonMapper)).addPathPatterns("/**").order(0);

        registry.addInterceptor(new LoginInterceptor()).excludePathPatterns(
                "/users/**",
                "/books/upload"
        ).order(1);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //这里可以设置全局的父接口如/api如 /api/**
                //是否发送Cookie
                .allowCredentials(true)
                //放行哪些原始域
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}
