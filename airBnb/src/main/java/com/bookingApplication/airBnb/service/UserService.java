package com.bookingApplication.airBnb.service;

import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.exceptions.UnAuthorisedException;
import com.bookingApplication.airBnb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity getUserById(Long id) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user + "user when getting all users");
        if(!user.getRoles().contains("ROLE_ADMIN")){
            throw new UnAuthorisedException("Only admins can view the users details");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
