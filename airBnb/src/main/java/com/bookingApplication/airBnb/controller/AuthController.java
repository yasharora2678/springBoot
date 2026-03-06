package com.bookingApplication.airBnb.controller;

import com.bookingApplication.airBnb.dto.LoginRequest;
import com.bookingApplication.airBnb.dto.SignUpRequest;
import com.bookingApplication.airBnb.dto.TokenResponse;
import com.bookingApplication.airBnb.entity.UserEntity;
import com.bookingApplication.airBnb.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.valves.ProxyErrorReportValve;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final  AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest body) {
        authService.signUp(body);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
