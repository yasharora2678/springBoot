package com.custom.interceptor.Service;

import org.springframework.stereotype.Service;

import com.custom.interceptor.Annotations.CustomAnnotation;

@Service
public class UserService {

    @CustomAnnotation(key = "userKey1" , intKey = 10, intArray = {12, 23})
    public String getUserDetails() {
        return "User Details";
    }
    
}
