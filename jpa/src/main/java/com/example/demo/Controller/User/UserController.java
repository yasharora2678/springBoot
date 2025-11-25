package com.example.demo.Controller.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Repository.UserRepository;

@Controller
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user) {  
        userRepository.createUser(user.getName(), user.getAge());
        return ResponseEntity.status(HttpStatus.OK)
                .body("User created successfully"); 
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
