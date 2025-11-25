package com.example.demo.Controller.User;

import com.example.demo.Dto.UserDetailsDTO;
import com.example.demo.Entity.UserDetails;
import com.example.demo.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserDetailsController {
    private final UserService userService;

    public UserDetailsController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<UserDetails> createUser(@RequestBody UserDetails user) {
        UserDetails response = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> getUsers(@PathVariable Long id) {
        UserDetailsDTO response =  userService.getAllUsers(id).toDto();
        return ResponseEntity.status(HttpStatus.OK)
        .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetails> updateUser(@PathVariable Long id , @RequestBody UserDetails user) {
        UserDetails response = userService.updateUser(id, user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}

