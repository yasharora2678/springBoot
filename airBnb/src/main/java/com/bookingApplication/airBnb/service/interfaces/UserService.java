package com.bookingApplication.airBnb.service.interfaces;

import com.bookingApplication.airBnb.dto.ProfileUpdateRequestDTO;
import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.interfaces.SignUpResponse;

public interface UserService {
    UserEntity getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDTO profileUpdateRequestDto);

    SignUpResponse getMyProfile();
}