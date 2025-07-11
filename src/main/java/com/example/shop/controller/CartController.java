package com.example.shop.controller;

import com.example.shop.dto.CartItemDTO;
import com.example.shop.dto.CartItemRequest;
import com.example.shop.model.ApiReponse;
import com.example.shop.model.CartItem;
import com.example.shop.service.CartItemServiceImpl;
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
    private final CartItemServiceImpl cartItemService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiReponse<List<CartItemDTO>>> getAllCartitems(@PathVariable Long userId) {
        return cartItemService.getAllCartItems(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiReponse<CartItemDTO>> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        return cartItemService.addItemToCart(cartItemRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiReponse<CartItemDTO>> updateItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        return cartItemService.updateItemToCart(cartItemRequest);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<ApiReponse<String>> deleteItem(@RequestParam Long userId,@RequestParam Long bookId) {
        return cartItemService.deleteItem(userId, bookId);
    }


    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiReponse<String>> deleteAllCartItems(@RequestParam Long userId) {
        return cartItemService.deleteAllCartItems(userId);
    }
}
