package com.example.shop.service;

import com.example.shop.dto.WishlistDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Wishlist;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WishlistService {
    ResponseEntity<ApiResponse<List<WishlistDTO>>> getWishlistsByUserId(Long userId);
    ResponseEntity<ApiResponse<WishlistDTO>> addToWishlist( WishlistDTO wishlistDTO);
    ResponseEntity<ApiResponse<WishlistDTO>> removeFromWishlist( WishlistDTO wishlistDTO);
}
