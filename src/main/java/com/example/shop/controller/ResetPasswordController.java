package com.example.shop.controller;

import com.example.shop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
