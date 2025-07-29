package com.example.shop.repository;

import com.example.shop.dto.ReviewDTO;
import com.example.shop.model.ReviewBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewBook, Long> {

    ReviewBook findReviewById(Long id);
    List<ReviewBook> findReviewsByBookId(Long id);
    List<ReviewBook> findReviewByUserId(Long id);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
