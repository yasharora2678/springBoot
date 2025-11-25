package com.custom.interceptor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.custom.interceptor.Service.UserService;

@Controller
@RequestMapping(value = "/api")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getUser")
    public ResponseEntity<String> getUserDetails() {
        String response = userService.getUserDetails();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
