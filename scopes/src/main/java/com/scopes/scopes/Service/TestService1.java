package com.scopes.scopes.Service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@Scope("singleton")
public class TestService1 {
    public TestService1() {
        System.out.println("Test Service1 initialization done");
    }

    @PostConstruct
    public void init() {
        System.out.println("Test Service1 object hashcode : " + this.hashCode());
    }
}
