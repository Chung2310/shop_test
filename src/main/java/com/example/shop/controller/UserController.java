package com.example.shop.controller;

import com.example.shop.model.user.UserEntityDTO;
import com.example.shop.model.auth.ChangePasswordRequest;
import com.example.shop.model.user.UserUpdateRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<UserEntityDTO>> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.updateUser(userUpdateRequest);
    }

    @PostMapping("/changePass")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return userService.changePassword(changePasswordRequest);
    }

    @PostMapping("/uploadImage/{id}")
    ResponseEntity<ApiResponse<String>> uploadAvatar(@PathVariable Long id, @RequestParam String mode, @RequestPart("image") MultipartFile file) {
        return userService.uploadImage(id,mode,file);
    }
}

