package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers();
    public ResponseEntity<ApiResponse<User>> setRoleAdmin(Long userId);
    public ResponseEntity<ApiResponse<String>> deleteUser(Long userId);

}
