package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.Orders;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    ResponseEntity<ApiResponse<Orders>> saveOrder(Orders order);
    Optional<Orders> getOrderById(Long orderId);
    ResponseEntity<ApiResponse<List<Orders>>> getAllOrders();

}
