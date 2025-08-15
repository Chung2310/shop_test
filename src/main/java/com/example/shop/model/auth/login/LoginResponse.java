package com.example.shop.model.auth.login;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String  fullName;
    private String token;
    private String email;
    private String phone;
    private String address;
    private String avatarUrl;
    private String refreshToken;
    private String backgroundUrl;
}
