package com.example.shop.repository;

import com.example.shop.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findReviewById(Long id);
    List<Review> findReviewsByProductId(Long id);
    List<Review> findReviewByUserEntityId(Long id);
    boolean existsByUserEntityIdAndProductId(Long userId, Long bookId);
}
