package com.example.shop.repository;

import com.example.shop.model.review.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findReviewLikeByUserEntityIdAndReviewId(Long Userid, Long reviewId);
    Long countByReviewId(Long reviewId);
    boolean existsReviewLikeByUserEntityIdAndReviewId(Long userId, Long reviewId);
}
