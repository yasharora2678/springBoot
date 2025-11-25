package com.scopes.scopes.Controller;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scopes.scopes.Service.TestService2;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping(value = "/api")
// @Scope("prototype")
// @Scope("request")
public class TestController2 {

    @Autowired
    TestService2 testService2;

    public TestController2() {
        System.out.println("Test Controller 2 initialization done");
    }

    @PostConstruct
    public void init() {
        System.out.println("Test controller2 object hashcode : " + this.hashCode() + " Test Service2 object hashcode: "
                + testService2.hashCode());
    }

    @GetMapping(path = "/fetchUser2")
    public ResponseEntity<String> getUserDetails() {
        System.out.println("Fetch User 2 api invoked from test controller 2" + " " + testService2.hashCode());
        return ResponseEntity.status(HttpStatus.OK).body("User Details");
    }
}
