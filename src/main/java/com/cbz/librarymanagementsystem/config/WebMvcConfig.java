package com.cbz.librarymanagementsystem.config;

import com.cbz.librarymanagementsystem.controller.LoginInterceptor;
import com.cbz.librarymanagementsystem.controller.SystemInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new SystemInterceptor(redisTemplate)).addPathPatterns("/**").order(0);

        registry.addInterceptor(new LoginInterceptor()).excludePathPatterns(
                "/users/**",
                "/*.html",
                "/css/**",
                "/js/**"
        ).order(1);
    }
}
