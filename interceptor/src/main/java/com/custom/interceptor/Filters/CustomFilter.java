package com.custom.interceptor.Filters;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CustomFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        System.out.println("🌐 Incoming request: " + req.getMethod() + " " + req.getRequestURI());

        // Continue filter chain (VERY IMPORTANT)
        chain.doFilter(request, response);

        System.out.println("✅ Response sent back for: " + req.getRequestURI());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
