package com.example.shop.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Long id;
    private String oldPassowrd;
    private String newPassowrd;
}
