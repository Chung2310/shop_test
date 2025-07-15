package com.example.shop.service;

import com.example.shop.dto.CartItemDTO;

import com.example.shop.dto.request.CartItemRequest;
import com.example.shop.model.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartItemService {
    ResponseEntity<ApiResponse<List<CartItemDTO>>> getAllCartItems(Long userId);
    ResponseEntity<ApiResponse<CartItemDTO>> addItemToCart(CartItemRequest cartItemRequest);
    ResponseEntity<ApiResponse<CartItemDTO>> updateItemToCart(CartItemRequest cartItemRequest);
    ResponseEntity<ApiResponse<String>> deleteItem(Long userId, Long bookId);
    ResponseEntity<ApiResponse<String>> deleteAllCartItems(Long userId);
}
