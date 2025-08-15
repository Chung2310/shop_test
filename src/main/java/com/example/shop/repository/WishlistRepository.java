package com.example.shop.repository;

import com.example.shop.model.wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserEntityId(Long userId);
}
