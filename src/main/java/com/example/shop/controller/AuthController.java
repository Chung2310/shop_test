package com.example.shop.controller;

import com.example.shop.model.auth.login.LoginResponse;
import com.example.shop.model.auth.register.RegisterRequest;
import com.example.shop.model.auth.login.LoginRequest;
import com.example.shop.model.auth.RefreshTokenRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.user.UserResponse;
import com.example.shop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<RefreshTokenRequest>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/forgot")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        return authService.forgotPassword(email);
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam Map<String, String> body) {
        return  authService.resetPassword(body);
    }
}
