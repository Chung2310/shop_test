package com.example.shop.repository;

import com.example.shop.model.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserEntityIdAndProductId(Long userId, Long bookId);
    List<CartItem> findByUserEntityId(Long userId);
    void deleteByUserEntityId(Long userId);
}
