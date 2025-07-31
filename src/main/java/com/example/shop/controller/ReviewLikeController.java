package com.example.shop.controller;

import com.example.shop.dto.ReviewDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.service.ReviewLikeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviewLike")
public class ReviewLikeController {

    @Autowired
    private ReviewLikeServiceImpl reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> toggleLike(@RequestParam Long userId, @RequestParam Long reviewId) {
        return reviewService.toggleLike(userId, reviewId);
    }

}
