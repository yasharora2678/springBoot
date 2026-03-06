package com.app.config;

import com.app.entity.UserEntity;
import com.app.entity.UserEntity.AuthProvider;
import com.app.entity.UserEntity.Role;
import com.app.repository.UserRepository;
import com.app.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Handles successful Google OAuth2 login.
 * Creates or fetches the user, generates JWT tokens,
 * sets refresh token in HttpOnly Cookie, redirects with access token.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.refresh-token-expiry-ms}")
    private long refreshTokenExpiryMs;

    // Where to redirect the frontend after Google login
    // Change this to your frontend URL
    private static final String FRONTEND_REDIRECT_URL = "http://localhost:3000/oauth2/success";

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email    = oAuth2User.getAttribute("email");
        String name     = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        // Find or create user
        UserEntity user = userRepository
                .findByProviderAndProviderId(AuthProvider.GOOGLE, googleId)
                .orElseGet(() -> {
                    // Check if email already registered (LOCAL user)
                    return userRepository.findByEmail(email).orElseGet(() -> {
                        UserEntity newUser = UserEntity.builder()
                                .name(name)
                                .email(email)
                                .role(Role.ROLE_GUEST)
                                .provider(AuthProvider.GOOGLE)
                                .providerId(googleId)
                                .enabled(true)
                                .build();
                        return userRepository.save(newUser);
                    });
                });

        // Generate tokens
        UserDetails userDetails = User.builder()
                .username(user.getEmail())
                .password("")
                .roles(user.getRole().name().replace("ROLE_", ""))
                .build();

        String accessToken  = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Store hashed refresh token
        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        user.setRefreshTokenExpiry(Instant.now().plusMillis(refreshTokenExpiryMs));
        userRepository.save(user);

        // Set HttpOnly refresh token cookie
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (refreshTokenExpiryMs / 1000));
        response.addCookie(cookie);

        log.info("Google OAuth2 login success for: {}", email);

        // Redirect to frontend with access token as query param
        // Frontend should read this once and store it in memory
        getRedirectStrategy().sendRedirect(
                request, response,
                FRONTEND_REDIRECT_URL + "?token=" + accessToken
        );
    }
}
