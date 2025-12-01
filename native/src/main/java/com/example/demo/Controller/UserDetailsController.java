package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.UserDetails;
import com.example.demo.Service.UserDetailsService;

@RestController
@RequestMapping("/api/user")
public class UserDetailsController {
    @Autowired
    UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<UserDetails> createUser(@RequestBody UserDetails user) {
        UserDetails response = userDetailsService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam(required = false) String name) {
        List<UserDTO> response = userDetailsService.getSpecificUserDetailsByPhoneCriteriaAPI(name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
