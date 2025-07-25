package com.example.shop.service;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.request.ChangePasswordRequest;
import com.example.shop.dto.request.UserUpdateRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User findUserById(Long userId);
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User saveUser(User user);
    ResponseEntity<ApiResponse<UserDTO>> updateUser(UserUpdateRequest userUpdateRequest);
    ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordRequest changePasswordRequest);
}
