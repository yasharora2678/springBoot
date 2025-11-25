package com.jpa.relations.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpa.relations.Entity.Users;
import com.jpa.relations.Repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public Users getUser(Long Id) {
        return userRepository.findById(Id).get();
    }
}
