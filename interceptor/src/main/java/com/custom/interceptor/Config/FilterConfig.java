package com.custom.interceptor.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

import com.custom.interceptor.Filters.CustomFilter;

public class FilterConfig {
    public FilterRegistrationBean<CustomFilter> loggingFilter() {
        FilterRegistrationBean<CustomFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CustomFilter());
        registrationBean.addUrlPatterns("/api/*"); // apply only for /api/*
        registrationBean.setOrder(1); // order among multiple filters

        return registrationBean;
    }
}
