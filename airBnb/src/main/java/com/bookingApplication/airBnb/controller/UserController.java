package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable Long id) {
        UserEntity response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
}
