package com.example.shop.controller;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.request.LoginRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import com.example.shop.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
