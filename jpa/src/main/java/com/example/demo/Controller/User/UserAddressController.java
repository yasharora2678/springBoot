package com.example.demo.Controller.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Entity.UserAddress;
import com.example.demo.Service.UserAddressService;

@Controller
@RequestMapping("/api/user-address")
public class UserAddressController {
    private final UserAddressService userAddressService;

    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserAddress> getUserAddress(@PathVariable long id) {
        UserAddress response = userAddressService.getUserAddress(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
