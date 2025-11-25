package com.scopes.scopes.Service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
// @Scope("prototype")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TestService2 {
    public TestService2() {
        System.out.println("Test Service2 initialization done");
    }

    @PostConstruct
    public void init() {
        System.out.println("Test Service2 object hashcode : " + this.hashCode());
    }
}
