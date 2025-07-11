package com.example.shop.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long bookId;
    private Long userId;
    private int quantity;
}
