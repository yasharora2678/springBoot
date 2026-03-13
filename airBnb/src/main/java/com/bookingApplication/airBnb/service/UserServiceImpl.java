package com.bookingApplication.airBnb.service;

import com.bookingApplication.airBnb.dto.ProfileUpdateRequestDTO;
import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.interfaces.SignUpResponse;
import com.bookingApplication.airBnb.repository.UserRepository;
import com.bookingApplication.airBnb.service.interfaces.UserService;
import com.bookingApplication.airBnb.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void updateProfile(ProfileUpdateRequestDTO profileUpdateRequestDto) {
        UserEntity user = AppUtils.getCurrentUser();
        log.info("Updating the profile for user with id: {}", user.getId());

        user.setName(profileUpdateRequestDto.getName());
        user.setGender(profileUpdateRequestDto.getGender());
        user.setDateOfBirth(profileUpdateRequestDto.getDateOfBirth());

        userRepository.save(user);
    }

    @Override
    public SignUpResponse getMyProfile() {
        UserEntity user = AppUtils.getCurrentUser();
        log.info("Getting the profile for user with id: {}", user.getId());
        return modelMapper.map(user, SignUpResponse.class);
    }
}
