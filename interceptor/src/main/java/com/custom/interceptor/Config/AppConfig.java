package com.custom.interceptor.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.custom.interceptor.Interceptors.CustomInterceptor;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    CustomInterceptor customInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor)
                .addPathPatterns("/api/*")
                .excludePathPatterns("/api/updateUser", "/api/deleteUser");
    }
}
