package com.example.demo.Service;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.UserAddress;
import com.example.demo.Repository.UserAddressRepository;


@Service
public class UserAddressService {
    private final UserAddressRepository userAddressRepository;

    public UserAddressService(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    public UserAddress getUserAddress(Long id) {
        return userAddressRepository.findById(id).get();
    }
}
