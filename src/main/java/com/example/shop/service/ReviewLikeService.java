package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ReviewLikeService {
    ResponseEntity<ApiResponse<Boolean>> toggleLike(Long userId, Long reviewId);
    Long getLikeCount(Long reviewId);
    boolean isLiked(Long userId, Long reviewId);
}
