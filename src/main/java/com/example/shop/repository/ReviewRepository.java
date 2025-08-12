package com.example.shop.repository;

import com.example.shop.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findReviewById(Long id);
    List<Review> findReviewsByProductId(Long id);
    List<Review> findReviewByUserId(Long id);
    boolean existsByUserIdAndProductId(Long userId, Long bookId);
}
