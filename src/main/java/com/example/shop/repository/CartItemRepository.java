package com.example.shop.repository;

import com.example.shop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserIdAndBookId(Long userId, Long bookId);
    List<CartItem> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
