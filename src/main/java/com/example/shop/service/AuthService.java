package com.example.shop.service;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.request.LoginRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {

     ResponseEntity<ApiResponse<UserDTO>> createUser(User user);

     ResponseEntity<ApiResponse<UserDTO>> login(LoginRequest loginRequest);
}
