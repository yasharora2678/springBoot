package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    UserService  userService;

    @GetMapping("/{$id}")
    public ResponseEntity<UserEntity> getUserByUsername(@RequestParam Long id) {
        UserEntity response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
}
