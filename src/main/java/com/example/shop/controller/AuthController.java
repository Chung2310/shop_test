package com.example.shop.controller;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.request.ChangePasswordRequest;
import com.example.shop.dto.request.LoginRequest;
import com.example.shop.dto.request.RefreshTokenRequest;
import com.example.shop.dto.request.UserUpdateRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import com.example.shop.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody LoginRequest loginRequest) {
        return authServiceImpl.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user) {
        return authServiceImpl.createUser(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenRequest>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authServiceImpl.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/update")
    ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return authServiceImpl.updateUser(userUpdateRequest);
    }

    @PostMapping("/changePassword")
    ResponseEntity<ApiResponse<String>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return authServiceImpl.changePassword(changePasswordRequest);
    }

    @PostMapping("/uploadAvatar/{id}")
    ResponseEntity<ApiResponse<String>> uploadAvatar(@PathVariable Long id, @RequestParam String mode, @RequestParam("avatar") MultipartFile file) {
        return authServiceImpl.uploadAvatar(id,mode,file);
    }

}
