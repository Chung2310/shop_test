package com.example.shop.controller;

import com.example.shop.model.wishlist.WishlistDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.wishlist.WishlistRequest;
import com.example.shop.model.wishlist.WishlistResponse;
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
    public ResponseEntity<ApiResponse<List<WishlistResponse>>> getWishlistsByUserId(@RequestParam Long userId) {
        return wishlistService.getWishlistsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WishlistDTO>> addWishlist(@RequestBody WishlistRequest wishlistRequest) {
        return wishlistService.addToWishlist(wishlistRequest);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<WishlistDTO>> removeWishlist(@RequestBody WishlistRequest wishlistRequest) {
        return wishlistService.removeFromWishlist(wishlistRequest);
    }
}
