package com.example.shop.controller;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.request.LoginRequest;
import com.example.shop.dto.request.RefreshTokenRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import com.example.shop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user) {
        return authService.register(user);
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
