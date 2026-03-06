package com.app.service;

import com.app.dto.AuthDtos.*;
import com.app.entity.UserEntity;
import com.app.entity.UserEntity.AuthProvider;
import com.app.entity.UserEntity.Role;
import com.app.exception.AppException;
import com.app.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.jwt.refresh-token-expiry-ms}")
    private long refreshTokenExpiryMs;

    // ─── SIGN UP ────────────────────────────────────────────────────────────────

    @Transactional
    public AuthResponse signup(SignupRequest request, HttpServletResponse response) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException("Email already registered", HttpStatus.CONFLICT);
        }

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_GUEST)         // default role as per flow
                .provider(AuthProvider.LOCAL)
                .enabled(true)
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());

        return buildAuthResponse(user, response);
    }

    // ─── LOGIN ──────────────────────────────────────────────────────────────────

    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        // This will throw BadCredentialsException if wrong credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        return buildAuthResponse(user, response);
    }

    // ─── REFRESH TOKEN ──────────────────────────────────────────────────────────

    @Transactional
    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshCookie(request)
                .orElseThrow(() -> new AppException("Refresh token missing", HttpStatus.UNAUTHORIZED));

        String email = jwtService.extractUsername(refreshToken);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.UNAUTHORIZED));

        // Validate refresh token against stored one
        if (user.getRefreshToken() == null ||
                !passwordEncoder.matches(refreshToken, user.getRefreshToken())) {
            throw new AppException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        if (user.getRefreshTokenExpiry().isBefore(Instant.now())) {
            throw new AppException("Refresh token expired. Please login again.", HttpStatus.UNAUTHORIZED);
        }

        return buildAuthResponse(user, response);
    }

    // ─── LOGOUT ─────────────────────────────────────────────────────────────────

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear stored refresh token
        extractRefreshCookie(request).ifPresent(token -> {
            try {
                String email = jwtService.extractUsername(token);
                userRepository.findByEmail(email).ifPresent(user -> {
                    user.setRefreshToken(null);
                    user.setRefreshTokenExpiry(null);
                    userRepository.save(user);
                });
            } catch (Exception ignored) {}
        });

        // Invalidate cookie on client
        Cookie cookie = new Cookie("refresh_token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        log.info("User logged out");
    }

    // ─── HELPERS ────────────────────────────────────────────────────────────────

    private AuthResponse buildAuthResponse(UserEntity user, HttpServletResponse response) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Store hashed refresh token in DB
        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        user.setRefreshTokenExpiry(Instant.now().plusMillis(refreshTokenExpiryMs));
        userRepository.save(user);

        // Set refresh token in HttpOnly cookie
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);    // not accessible via JS (XSS protection)
        cookie.setSecure(true);      // HTTPS only — disable in local dev if needed
        cookie.setPath("/");
        cookie.setMaxAge((int) (refreshTokenExpiryMs / 1000));
        response.addCookie(cookie);

        // Build response DTO (access token in body)
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setExpiresIn(jwtService.getAccessTokenExpiryMs() / 1000);

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getName());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(user.getRole().name());
        userInfo.setProvider(user.getProvider().name());
        authResponse.setUser(userInfo);

        return authResponse;
    }

    private Optional<String> extractRefreshCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(c -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
