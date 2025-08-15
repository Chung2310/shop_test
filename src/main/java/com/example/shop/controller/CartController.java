package com.example.shop.controller;

import com.example.shop.model.cart.CartItemDTO;
import com.example.shop.model.cart.CartResponse;
import com.example.shop.model.cart.CartItemRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartResponse>>> getAllCartItemsByUserId(@PathVariable Long userId) {
        return cartItemService.getAllCartItems(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        return cartItemService.addItemToCart(cartItemRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        return cartItemService.updateItemToCart(cartItemRequest);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<String>> deleteItem(@RequestParam Long userId,@RequestParam Long bookId) {
        return cartItemService.deleteItem(userId, bookId);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteAllCartItems(@RequestParam Long userId) {
        return cartItemService.deleteAllCartItems(userId);
    }
}
