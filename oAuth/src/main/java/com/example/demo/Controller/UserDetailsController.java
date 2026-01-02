package com.example.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDetailsController {
    @GetMapping("/")
    public String defaultMethod() {
        return "Hi, You are logged in!";
    }

    @GetMapping("/users")
    public String getUsers() {
        return "Users fetched successfully";
    }
}
