package com.example.shop.controller;

import com.example.shop.dto.WishlistDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<WishlistDTO>>> getWishlistsByUserId(Long userId) {
        return wishlistService.getWishlistsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WishlistDTO>> addWishlist( WishlistDTO wishlistDTO) {
        return wishlistService.addToWishlist(wishlistDTO);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<WishlistDTO>> removeWishlist( WishlistDTO wishlistDTO) {
        return wishlistService.removeFromWishlist(wishlistDTO);
    }
}
