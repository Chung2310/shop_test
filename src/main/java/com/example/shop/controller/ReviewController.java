package com.example.shop.controller;

import com.example.shop.dto.ReviewDTO;
import com.example.shop.dto.request.ReviewRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.createReview(reviewDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> deleteReviewById(@PathVariable Long id) {
        return reviewService.deleteReviewById(id);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(@RequestBody ReviewRequest reviewRequest) {
        return reviewService.updateReview(reviewRequest);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewByBookId(@PathVariable Long id) {
        return reviewService.getReviewsByBookId(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewByUserId(@PathVariable Long id) {
        return reviewService.getReviewsByUserId(id);
    }

    @PostMapping("/checkReviewed/{userId}")
    public ResponseEntity<ApiResponse<List<Long>>>  checkReviewed(@PathVariable Long userId, @RequestBody List<Long> bookIds) {
        return reviewService.checkReviewed(userId,bookIds);
    }
}
