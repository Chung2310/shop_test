package com.example.shop.service;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.request.ChangePasswordRequest;
import com.example.shop.dto.request.LoginRequest;
import com.example.shop.dto.request.UserUpdateRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

     ResponseEntity<ApiResponse<UserDTO>> createUser(User user);

     ResponseEntity<ApiResponse<UserDTO>> login(LoginRequest loginRequest);

     ResponseEntity<ApiResponse<UserDTO>> updateUser(UserUpdateRequest userUpdateRequest);

     ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordRequest changePasswordRequest);

     ResponseEntity<ApiResponse<String>> uploadAvatar(Long id, String mode,MultipartFile file);
}
