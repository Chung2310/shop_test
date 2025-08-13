package com.example.shop.controller;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.PasswordResetToken;
import com.example.shop.model.User;
import com.example.shop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class ResetPasswordController {
    @Autowired
    private AuthService authService;

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token,
                                        Map<String, Object> model) {
        model.put("token", token);
        return "reset-password";
    }

}
