package com.example.shop.service;

import com.example.shop.dto.ReviewDTO;
import com.example.shop.dto.request.ReviewRequest;
import com.example.shop.model.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewService {
    ResponseEntity<ApiResponse<ReviewDTO>> createReview(ReviewDTO dto);
    ResponseEntity<ApiResponse<ReviewDTO>> deleteReview(Long reviewId);
    ResponseEntity<ApiResponse<ReviewDTO>> updateReview(ReviewRequest reviewRequest);
    ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByBookId(Long bookId);
    ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByUserId(Long userId);
    ResponseEntity<ApiResponse<List<Long>>> checkReviewed(Long userId,  List<Long> bookIds);
}
