package com.example.shop.controller;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import com.example.shop.service.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private  AdminServiceImpl  adminServiceImpl;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUser() {
        return adminServiceImpl.getAllUsers();
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        return adminServiceImpl.deleteUser(id);
    }

    @PutMapping("/user/role/{id}")
    public ResponseEntity<ApiResponse<User>> setRoleAdmin(@PathVariable Long id) {
        return adminServiceImpl.setRoleAdmin(id);
    }
}
