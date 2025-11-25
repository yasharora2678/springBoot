package com.example.demo.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.UserDetails;
import com.example.demo.Repository.UserDetailsRepository;

import jakarta.transaction.Transactional;


@Service
public class UserService {
    private final UserDetailsRepository userRepository;

    public UserService(UserDetailsRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails getAllUsers(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    public UserDetails addUser(UserDetails user) {
        return userRepository.save(user);
    }

    @Transactional
    public UserDetails updateUser(Long id, UserDetails user) {
        Optional<UserDetails> existingUser = this.userRepository.findById(id);
        if(existingUser.isPresent()) {
            return userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public String deleteUser(Long id) {
        Optional<UserDetails> existingUser = this.userRepository.findById(id);
        if(existingUser.isPresent()) {
            userRepository.deleteById(id);
            return "User Deleted Successfully";
        }
        return "User not present";
    }
}