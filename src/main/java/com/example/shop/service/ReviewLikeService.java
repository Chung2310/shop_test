package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.Review;
import com.example.shop.model.ReviewLike;
import com.example.shop.repository.ReviewLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class ReviewLikeService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewLikeService.class);

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;


    public ResponseEntity<ApiResponse<Boolean>> toggleLike(Long userId, Long reviewId) {
        if(userId == null || reviewId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Thiếu thông tin yêu cầu", null));
        }

        var user = userService.findUserById(userId);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Không tìm thấy người dùng", null));
        }

        var review = reviewService.findReviewByID(reviewId);
        if(review == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Không tìm thấy đánh giá", null));
        }

        Optional<ReviewLike> existingLike = reviewLikeRepository.findReviewLikeByUserIdAndReviewId(userId, reviewId);

        boolean liked;
        String message;

        if(existingLike.isPresent()) {
            reviewLikeRepository.delete(existingLike.get());
            liked = false;
            message = "Bỏ like đánh giá thành công";
        } else {
            ReviewLike like = new ReviewLike();
            like.setUser(user);
            like.setReview(review);
            reviewLikeRepository.save(like);
            liked = true;
            message = "Like đánh giá thành công";
        }

        updateLikeCount(reviewId);
        logger.info("User {} {} review {}", userId, liked ? "liked" : "unliked", reviewId);

        return ResponseEntity.ok(new ApiResponse<>(200, message, liked));
    }


    public Long getLikeCount(Long reviewId) {
        long count = reviewLikeRepository.countByReviewId(reviewId);
        logger.debug("Số lượt like của review {} là {}", reviewId, count);
        return count;
    }

    public boolean isLiked(Long userId, Long reviewId) {
        boolean liked = reviewLikeRepository.existsReviewLikeByUserIdAndReviewId(userId, reviewId);
        logger.debug("Người dùng {} {} đánh giá {}", userId, liked ? "đã like" : "chưa like", reviewId);
        return liked;
    }

    public void updateLikeCount(Long reviewId) {
        logger.debug("Cập nhật số lượt like cho review {}", reviewId);

        if (reviewId == null) {
            logger.warn("reviewId null khi cập nhật like count");
            return;
        }

        Review review = reviewService.findReviewByID(reviewId);
        if (review == null) {
            logger.warn("Không tìm thấy review khi cập nhật like count cho reviewId {}", reviewId);
            return;
        }

        long count = reviewLikeRepository.countByReviewId(reviewId);
        review.setLikeCount(Math.toIntExact(count));
        reviewService.saveOrUpdateReview(review);

        logger.info("Đã cập nhật like count = {} cho review {}", count, reviewId);
    }
}
