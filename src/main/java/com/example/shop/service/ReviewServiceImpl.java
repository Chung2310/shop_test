package com.example.shop.service;

import com.example.shop.dto.ReviewDTO;
import com.example.shop.dto.mapper.ReviewMapper;
import com.example.shop.dto.request.ReviewRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.ReviewBook;
import com.example.shop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private ReviewMapper reviewMapper;

    private ReviewBook saveOrUpdateReview(ReviewBook reviewBook) {
        logger.info("Saving or updating review: {}", reviewBook);
        return reviewRepository.save(reviewBook);
    }

    @Override
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(ReviewDTO reviewDTO) {
        logger.info("Creating review with data: {}", reviewDTO);

        if (reviewDTO == null) {
            logger.warn("ReviewDTO is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin để tạo đánh giá!", null)
            );
        }

        if (userService.findUserById(reviewDTO.getUserDTO().getId()) == null) {
            logger.warn("User not found with id: {}", reviewDTO.getUserDTO().getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Người dùng không tồn tại!", null)
            );
        }

        if (bookService.findBookById(reviewDTO.getBookDTO().getId()) == null) {
            logger.warn("Book not found with id: {}", reviewDTO.getBookDTO().getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Sách không tồn tại!", null)
            );
        }

        ReviewBook reviewBook = new ReviewBook();
        reviewBook.setBook(bookService.findBookById(reviewDTO.getBookDTO().getId()));
        reviewBook.setUser(userService.findUserById(reviewDTO.getUserDTO().getId()));
        reviewBook.setRating(reviewDTO.getRating());
        reviewBook.setComments(reviewDTO.getComments());

        ReviewBook savedReview = saveOrUpdateReview(reviewBook);
        logger.info("Review created successfully: {}", savedReview);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(HttpStatus.CREATED.value(), "Tạo đánh giá thành công!", reviewMapper.toDto(savedReview))
        );
    }

    @Override
    public ResponseEntity<ApiResponse<ReviewDTO>> deleteReview(Long reviewId) {
        logger.info("Deleting review with id: {}", reviewId);

        if (reviewId == null) {
            logger.warn("ReviewId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin id đánh giá!", null)
            );
        }

        ReviewBook reviewBook = reviewRepository.findReviewById(reviewId);
        if (reviewBook == null) {
            logger.warn("Review not found with id: {}", reviewId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Lỗi khi lấy thông tin", null)
            );
        }

        reviewBook.setDeleted(true);
        reviewRepository.save(reviewBook);
        logger.info("Review marked as deleted: {}", reviewBook);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Xoá đánh giá thành công!", null)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(ReviewRequest reviewRequest) {
        logger.info("Updating review with data: {}", reviewRequest);

        if (reviewRequest.getId() == null || reviewRequest.getComments() == null || reviewRequest.getRating() == 0) {
            logger.warn("Incomplete update request: {}", reviewRequest);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!", null)
            );
        }

        ReviewBook reviewBook = reviewRepository.findReviewById(reviewRequest.getId());
        if (reviewBook == null) {
            logger.warn("Review not found with id: {}", reviewRequest.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thông tin đánh giá", null)
            );
        }

        reviewBook.setComments(reviewRequest.getComments());
        reviewBook.setRating(reviewRequest.getRating());
        reviewRepository.save(reviewBook);
        logger.info("Review updated successfully: {}", reviewBook);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Sửa thông tin đánh giá thành công!", null)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByBookId(Long bookId) {
        logger.info("Getting reviews for bookId: {}", bookId);

        if (bookId == null) {
            logger.warn("BookId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!", null)
            );
        }

        List<ReviewBook> reviewBooks = reviewRepository.findReviewsByBookId(bookId);
        if (reviewBooks == null) {
            logger.warn("Review list is null for bookId: {}", bookId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Lỗi khi lấy thông tin", null)
            );
        }

        List<ReviewBook> filteredReviews = new ArrayList<>();
        for (ReviewBook rb : reviewBooks) {
            if (!rb.isDeleted()) {
                filteredReviews.add(rb);
            }
        }

        List<ReviewDTO> reviewDTOs = reviewMapper.toDtoList(filteredReviews);
        logger.info("Returning {} active reviews for bookId: {}", reviewDTOs.size(), bookId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách đánh giá của sản phẩm thành công", reviewDTOs)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByUserId(Long userId) {
        logger.info("Getting reviews for userId: {}", userId);

        if (userId == null) {
            logger.warn("UserId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!", null)
            );
        }

        List<ReviewBook> reviewBooks = reviewRepository.findReviewByUserId(userId);
        if (reviewBooks == null) {
            logger.warn("Review list is null for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Lỗi khi lấy thông tin", null)
            );
        }

        List<ReviewBook> filteredReviews = new ArrayList<>();
        for (ReviewBook rb : reviewBooks) {
            if (!rb.isDeleted()) {
                filteredReviews.add(rb);
            }
        }

        List<ReviewDTO> reviewDTOs = reviewMapper.toDtoList(filteredReviews);
        logger.info("Returning {} active reviews for userId: {}", reviewDTOs.size(), userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách đã đánh giá của người dùng thành công", reviewDTOs)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<List<Long>>> checkReviewed(Long userId, List<Long> bookIds) {
        logger.info("📥 [checkReviewed] Bắt đầu xử lý yêu cầu kiểm tra review");
        logger.info("➡️  Dữ liệu nhận vào: userId = {}, bookIds = {}",
                userId != null ? userId : "null",
                bookIds != null ? bookIds : "null");

        if (userId == null || bookIds == null) {
            logger.warn("⚠️  Dữ liệu truyền vào bị thiếu (userId hoặc bookIds bị null)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu dữ liệu yêu cầu", null)
            );
        }

        if (bookIds.isEmpty()) {
            logger.warn("⚠️  Danh sách bookIds rỗng");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Danh sách sách trống", null)
            );
        }

        logger.info("🔍 Tiến hành kiểm tra từng bookId chưa được đánh giá...");
        List<Long> notReviewed = new ArrayList<>();
        for (Long bookId : bookIds) {
            boolean exists = reviewRepository.existsByUserIdAndBookId(userId, bookId);
            logger.debug("🔸 Book ID = {} | Đã review: {}", bookId, exists);
            if (!exists) {
                notReviewed.add(bookId);
            }
        }

        logger.info("✅ Kiểm tra hoàn tất. Tổng số sách chưa được review: {}", notReviewed.size());
        logger.debug("📄 Danh sách bookId chưa review: {}", notReviewed);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Kiểm tra hoàn tất!", notReviewed)
        );
    }



}
