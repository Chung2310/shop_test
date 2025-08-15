package com.example.shop.service;

import com.example.shop.model.Messages;
import com.example.shop.model.ResponseHandler;
import com.example.shop.model.review.ReviewDTO;
import com.example.shop.mapper.ReviewMapper;
import com.example.shop.model.review.ReviewRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.review.Review;
import com.example.shop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService bookService;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    @Lazy
    private ReviewLikeService reviewLikeService;

    public Review saveOrUpdateReview(Review review) {
        logger.info("Saving or updating review: {}", review);
        return reviewRepository.save(review);
    }

    public Review findReviewByID(Long id) {
        return reviewRepository.findReviewById(id);
    }

    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(ReviewDTO reviewDTO) {
        logger.info("Creating review with data: {}", reviewDTO);

        if (reviewDTO == null) {
            logger.warn("ReviewDTO is null");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        if (userService.findUserById(reviewDTO.getUserDTO().getId()) == null) {
            logger.warn("User not found with id: {}", reviewDTO.getUserDTO().getId());
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        if (bookService.findBookById(reviewDTO.getProductDTO().getId()) == null) {
            logger.warn("Book not found with id: {}", reviewDTO.getProductDTO().getId());
            return ResponseHandler.generateResponse(Messages.PRODUCT_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        if (reviewRepository.existsByUserEntityIdAndProductId(reviewDTO.getUserDTO().getId(), reviewDTO.getProductDTO().getId())) {
            return ResponseHandler.generateResponse(Messages.PRODUCT_ALREADY_REVIEWED,HttpStatus.CONFLICT, null);
        }
        else {
            Review review = new Review();
            review.setProduct(bookService.findBookById(reviewDTO.getProductDTO().getId()));
            review.setUser(userService.findUserById(reviewDTO.getUserDTO().getId()));
            review.setRating(reviewDTO.getRating());
            review.setComments(reviewDTO.getComments());

            Review savedReview = saveOrUpdateReview(review);
            logger.info("Review created successfully: {}", savedReview);

            return ResponseHandler.generateResponse(Messages.REVIEW_CREATED,HttpStatus.CREATED, reviewMapper.toDto(savedReview));
        }


    }

    public ResponseEntity<ApiResponse<ReviewDTO>> deleteReviewById(Long reviewId) {
        logger.info("Deleting review with id: {}", reviewId);

        if (reviewId == null) {
            logger.warn("ReviewId is null");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) {
            logger.warn("Review not found with id: {}", reviewId);
            return ResponseHandler.generateResponse(Messages.REVIEW_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        review.setDeleted(true);
        reviewRepository.save(review);
        logger.info("Review marked as deleted: {}", review);

        return ResponseHandler.generateResponse(Messages.REVIEW_DELETED,HttpStatus.NO_CONTENT, null);
    }

    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(ReviewRequest reviewRequest) {
        logger.info("Updating review with data: {}", reviewRequest);

        if (reviewRequest.getId() == null || reviewRequest.getComments() == null || reviewRequest.getRating() == 0) {
            logger.warn("Incomplete update request: {}", reviewRequest);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        Review review = reviewRepository.findReviewById(reviewRequest.getId());
        if (review == null) {
            logger.warn("Review not found with id: {}", reviewRequest.getId());
            return ResponseHandler.generateResponse(Messages.REVIEW_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        review.setComments(reviewRequest.getComments());
        review.setRating(reviewRequest.getRating());
        reviewRepository.save(review);
        logger.info("Review updated successfully: {}", review);

        return ResponseHandler.generateResponse(Messages.REVIEW_UPDATED,HttpStatus.OK, null);
    }

    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByBookId(Long bookId) {
        logger.info("Getting reviews for bookId: {}", bookId);

        if (bookId == null) {
            logger.warn("BookId is null");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        List<Review> reviews = reviewRepository.findReviewsByProductId(bookId);
        if (reviews == null) {
            logger.warn("Review list is null for bookId: {}", bookId);
            return ResponseHandler.generateResponse(Messages.REVIEW_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        List<Review> filteredReviews = new ArrayList<>();
        for (Review rb : reviews) {
            if (!rb.isDeleted()  ) {
                if(reviewLikeService.isLiked(rb.getUser().getId(),rb.getId())) {
                    rb.setLikedByCurrentUser(true);
                }
                else {
                    rb.setLikedByCurrentUser(false);
                }
                filteredReviews.add(rb);
            }
        }

        List<ReviewDTO> reviewDTOs = reviewMapper.toDtoList(filteredReviews);
        logger.info("Returning {} active reviews for bookId: {}", reviewDTOs.size(), bookId);

        return ResponseHandler.generateResponse(Messages.REVIEW_FETCH_SUCCESS,HttpStatus.OK, reviewDTOs);
    }

    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByUserId(Long userId) {
        logger.info("Getting reviews for userId: {}", userId);

        if (userId == null) {
            logger.warn("UserId is null");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        List<Review> reviews = reviewRepository.findReviewByUserEntityId(userId);
        if (reviews == null) {
            logger.warn("Review list is null for userId: {}", userId);
            return ResponseHandler.generateResponse(Messages.REVIEW_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        List<Review> filteredReviews = new ArrayList<>();
        for (Review rb : reviews) {
            if (!rb.isDeleted()) {
                filteredReviews.add(rb);
            }
        }

        List<ReviewDTO> reviewDTOs = reviewMapper.toDtoList(filteredReviews);
        logger.info("Returning {} active reviews for userId: {}", reviewDTOs.size(), userId);

        return ResponseHandler.generateResponse(Messages.REVIEW_FETCH_SUCCESS,HttpStatus.OK, reviewDTOs);
    }

    public ResponseEntity<ApiResponse<List<Long>>> checkReviewed(Long userId, List<Long> bookIds) {
        logger.info(" [checkReviewed] Bắt đầu xử lý yêu cầu kiểm tra review");
        logger.info("  Dữ liệu nhận vào: userId = {}, bookIds = {}",
                userId != null ? userId : "null",
                bookIds != null ? bookIds : "null");

        if (userId == null || bookIds == null) {
            logger.warn("  Dữ liệu truyền vào bị thiếu (userId hoặc bookIds bị null)");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        if (bookIds.isEmpty()) {
            logger.warn("  Danh sách bookIds rỗng");
            return ResponseHandler.generateResponse(Messages.REVIEW_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        logger.info(" Tiến hành kiểm tra từng bookId chưa được đánh giá...");
        List<Long> notReviewed = new ArrayList<>();
        for (Long bookId : bookIds) {
            boolean exists = reviewRepository.existsByUserEntityIdAndProductId(userId, bookId);
            logger.debug(" Book ID = {} | Đã review: {}", bookId, exists);
            if (!exists) {
                notReviewed.add(bookId);
            }
        }

        logger.info("Kiểm tra hoàn tất. Tổng số sách chưa được review: {}", notReviewed.size());
        logger.info("  Dữ liệu đầu ra: bookIds = {}",
                bookIds != null ? notReviewed : "null");
        logger.debug(" Danh sách bookId chưa review: {}", notReviewed);

        return ResponseHandler.generateResponse(Messages.REVIEW_CHECKED,HttpStatus.OK, notReviewed);
    }
}
