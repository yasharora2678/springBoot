# 🔐 Authentication Flow — Testing Guide

## ⚙️ Setup

### 1. Configure `application.yml`
Before running, update these values:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/authdb
    username: YOUR_DB_USERNAME
    password: YOUR_DB_PASSWORD

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_GOOGLE_CLIENT_ID       # from Google Cloud Console
            client-secret: YOUR_GOOGLE_CLIENT_SECRET

app:
  jwt:
    secret: YOUR_BASE64_SECRET   # must be 256+ bits (32+ bytes base64 encoded)
```

### Generate a JWT secret (run once):
```bash
openssl rand -base64 64
```

### Google OAuth2 Setup:
1. Go to https://console.cloud.google.com
2. Create a new project → APIs & Services → Credentials
3. Create OAuth 2.0 Client ID (Web application)
4. Add Authorized redirect URI: `http://localhost:8080/auth/oauth2/callback/google`
5. Copy Client ID and Secret to application.yml

---

## 🚀 Run the App
```bash
mvn spring-boot:run
```

---

## 🧪 Test with curl

### 1. Sign Up
```bash
curl -c cookies.txt -X POST http://localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```
**Expected:** 201 Created, `{ "accessToken": "...", "user": { "role": "ROLE_GUEST", ... } }`
**Cookie:** `refresh_token` set as HttpOnly

---

### 2. Login
```bash
curl -c cookies.txt -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```
**Expected:** 200 OK with access token in body

---

### 3. Access Protected Route (use access token from login)
```bash
curl -X GET http://localhost:8080/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```
**Expected:** 200 OK with user info
**Without token:** 403 Forbidden

---

### 4. Refresh Token (uses HttpOnly cookie automatically)
```bash
curl -b cookies.txt -X POST http://localhost:8080/auth/refresh
```
**Expected:** 200 OK with new access token

---

### 5. Logout
```bash
curl -b cookies.txt -X POST http://localhost:8080/auth/logout
```
**Expected:** 200 OK, refresh cookie cleared

---

### 6. Wrong Password Test
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "email": "john@example.com", "password": "wrongpass" }'
```
**Expected:** 401 Unauthorized, `"Invalid email or password"`

---

### 7. Google OAuth2 Login
Open in browser:
```
http://localhost:8080/auth/oauth2/authorize/google
```
Completes Google login, redirects to `http://localhost:3000/oauth2/success?token=...`

---

## 📋 API Summary

| Method | Endpoint              | Auth Required | Description              |
|--------|-----------------------|---------------|--------------------------|
| POST   | /auth/signup          | ❌            | Register new user        |
| POST   | /auth/login           | ❌            | Login with email/pass    |
| POST   | /auth/refresh         | Cookie        | Get new access token     |
| POST   | /auth/logout          | Cookie        | Logout + clear tokens    |
| GET    | /auth/me              | ✅ Bearer     | Get current user         |
| GET    | /auth/oauth2/authorize/google | ❌   | Start Google OAuth2      |

---

## 🏗️ Project Structure
```
src/main/java/com/app/
├── AuthApplication.java
├── config/
│   ├── SecurityConfig.java         ← Spring Security config
│   └── OAuth2SuccessHandler.java   ← Google login handler
├── controller/
│   └── AuthController.java         ← All auth endpoints
├── dto/
│   └── AuthDtos.java               ← Request/Response DTOs
├── entity/
│   └── UserEntity.java             ← User DB entity
├── exception/
│   ├── AppException.java
│   └── GlobalExceptionHandler.java
├── filter/
│   └── JWTAuthFilter.java          ← JWT interceptor (runs every request)
├── repository/
│   └── UserRepository.java
└── service/
    ├── AuthService.java            ← Signup/Login/Refresh logic
    ├── CustomUserDetailsService.java
    └── JwtService.java             ← Token generation & validation
```

## 🔒 Security Notes
- Access Token: **15 min** expiry, sent in response body
- Refresh Token: **7 days** expiry, stored in **HttpOnly Cookie** (XSS-proof)
- Refresh tokens are **hashed** in the database (BCrypt)
- `JWTAuthFilter` checks `Authorization: Bearer <token>` on every request
- Google OAuth2 users get `ROLE_GUEST` on first login (same as signup)
