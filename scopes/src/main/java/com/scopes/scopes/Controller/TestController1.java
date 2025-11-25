package com.scopes.scopes.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scopes.scopes.Service.TestService1;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@Scope("session")
@RequestMapping(value = "/api")
public class TestController1 {

    @Autowired
    TestService1 testService1;

    public TestController1() {
        System.out.println("Test Controller 1 initialization done");
    }

    @PostConstruct
    public void init() {
        System.out.println("Test controller1 object hashcode : " + this.hashCode() + " Test Service1 object hashcode: "
                + testService1.hashCode());
    }

    @GetMapping(path = "/fetchUser1")
    public ResponseEntity<String> getUserDetails() {
        System.out.println("Fetch User 1 api invoked");
        System.out.println("Test controller1 object hashcode : " + this.hashCode() + " Test Service1 object hashcode: "
                + testService1.hashCode());
        return ResponseEntity.status(HttpStatus.OK).body("User Details");
    }

    @GetMapping(path = "/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        System.out.println("Ending the session");
        HttpSession session = request.getSession();
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("User Logged Out");
    }
}
