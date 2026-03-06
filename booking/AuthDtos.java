package com.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// ─── Sign Up ───────────────────────────────────────────────────────────────
public class AuthDtos {

    @Data
    public static class SignupRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;
    }

    // ─── Login ─────────────────────────────────────────────────────────────────
    @Data
    public static class LoginRequest {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }

    // ─── Auth Response (returned on login / signup) ────────────────────────────
    @Data
    public static class AuthResponse {
        private String accessToken;
        private String tokenType = "Bearer";
        private long expiresIn;  // seconds
        private UserInfo user;

        // Note: refreshToken is in HttpOnly Cookie — NOT here

        @Data
        public static class UserInfo {
            private Long id;
            private String name;
            private String email;
            private String role;
            private String provider;
        }
    }

    // ─── Refresh Token Request ─────────────────────────────────────────────────
    @Data
    public static class RefreshRequest {
        // token comes from cookie automatically — no body needed
    }

    // ─── Generic Message Response ──────────────────────────────────────────────
    @Data
    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }
}
