package com.app.controller;

import com.app.dto.AuthDtos.*;
import com.app.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Auth Controller
 *
 * POST /auth/signup         → Register (creates user with ROLE_GUEST)
 * POST /auth/login          → Login (returns access token + sets refresh cookie)
 * POST /auth/refresh        → Get new access token via refresh cookie
 * POST /auth/logout         → Clear tokens
 * GET  /auth/me             → Get current user info (protected)
 *
 * GET  /auth/oauth2/authorize/google  → Initiates Google login (handled by Spring)
 * GET  /auth/oauth2/callback/google   → Google callback (handled by Spring + OAuth2SuccessHandler)
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ─── POST /auth/signup ─────────────────────────────────────────────────────
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.signup(request, response);
        return ResponseEntity.status(201).body(authResponse);
    }

    // ─── POST /auth/login ──────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.login(request, response);
        return ResponseEntity.ok(authResponse);
    }

    // ─── POST /auth/refresh ────────────────────────────────────────────────────
    // Refresh token is read from HttpOnly cookie automatically
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.refresh(request, response);
        return ResponseEntity.ok(authResponse);
    }

    // ─── POST /auth/logout ─────────────────────────────────────────────────────
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);
        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
    }

    // ─── GET /auth/me ──────────────────────────────────────────────────────────
    // Protected route — JWTAuthFilter validates Bearer token before reaching here
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(new MessageResponse("Authenticated as: " + userDetails.getUsername()));
    }

    // ─── GET /auth/oauth2/failure ──────────────────────────────────────────────
    @GetMapping("/oauth2/failure")
    public ResponseEntity<MessageResponse> oauthFailure() {
        return ResponseEntity.status(401).body(new MessageResponse("Google login failed"));
    }
}
