package com.example.shop.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private BookDTO book;
    private int quantity;
}
