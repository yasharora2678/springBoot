package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.interfaces.SignUpResponse;
import com.bookingApplication.airBnb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<SignUpResponse> getUserByUsername(@PathVariable Long id) {
        SignUpResponse response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }
}
