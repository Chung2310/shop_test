package com.example.shop.service;

import com.example.shop.dto.CartItemDTO;

import com.example.shop.dto.request.CartItemRequest;
import com.example.shop.model.ApiReponse;
import com.example.shop.model.CartItem;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartItemService {
    ResponseEntity<ApiReponse<List<CartItemDTO>>> getAllCartItems(Long userId);
    ResponseEntity<ApiReponse<CartItemDTO>> addItemToCart(CartItemRequest cartItemRequest);
    ResponseEntity<ApiReponse<CartItemDTO>> updateItemToCart(CartItemRequest cartItemRequest);
    ResponseEntity<ApiReponse<String>> deleteItem(Long userId, Long bookId);
    ResponseEntity<ApiReponse<String>> deleteAllCartItems(Long userId);
}
