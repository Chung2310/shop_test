package com.example.shop.repository;

import com.example.shop.model.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findReviewLikeByUserIdAndReviewId(Long Userid, Long reviewId);
    Long countByReviewId(Long reviewId);
    boolean existsReviewLikeByUserIdAndReviewId(Long userId, Long reviewId);
}
