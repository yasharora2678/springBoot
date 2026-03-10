package com.bookingApplication.airBnb.security;

import com.bookingApplication.airBnb.dto.LoginRequest;
import com.bookingApplication.airBnb.dto.SignUpRequest;
import com.bookingApplication.airBnb.interfaces.SignUpResponse;
import com.bookingApplication.airBnb.enums.Role;
import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        UserEntity user = UserEntity.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Set.of(signUpRequest.getRole()))
                .gender(signUpRequest.getGender())
                .dateOfBirth(LocalDate.parse(signUpRequest.getDateOfBirth()))
                .provider("LOCAL")
                .build();

        user = userRepository.save(user);
        return modelMapper.map(user, SignUpResponse.class);
    }

    public String[] login(LoginRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()
        ));

        UserEntity user = (UserEntity) authentication.getPrincipal();

        String[] arr = new String[2];
        arr[0] = jwtService.generateAccessToken(user);
        arr[1] = jwtService.generateRefreshToken(user);

        return arr;
    }

    public String refreshToken(String refreshToken) {
        Long userId = jwtService.extractUserId(refreshToken);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return jwtService.generateAccessToken(user);
    }
}
