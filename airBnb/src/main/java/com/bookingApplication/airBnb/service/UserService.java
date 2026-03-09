package com.bookingApplication.airBnb.service;

import com.bookingApplication.airBnb.interfaces.SignUpResponse;
import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.enums.Role;
import com.bookingApplication.airBnb.exceptions.UnAuthorisedException;
import com.bookingApplication.airBnb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public SignUpResponse getUserById(Long id) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.getRoles().contains(Role.HOTEL_MANAGER)){
            throw new UnAuthorisedException("Only admins can view the users details");
        }

        user =  userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return modelMapper.map(user, SignUpResponse.class);
    }
}
