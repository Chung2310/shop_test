package com.example.shop.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long bookId;
    private Long userId;
    private int quantity;
}
