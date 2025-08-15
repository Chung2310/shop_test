package com.example.shop.repository;

import com.example.shop.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByUserEntityId(Long userId);
    Order findOrderById(Long orderId);
}
