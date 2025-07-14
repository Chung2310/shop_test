package com.example.shop.service;

import com.example.shop.model.ApiReponse;
import com.example.shop.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    public ResponseEntity<ApiReponse<List<User>>> getAllUsers();
    public ResponseEntity<ApiReponse<User>> setRoleAdmin(Long userId);
    public ResponseEntity<ApiReponse<String>> deleteUser(Long userId);

}
