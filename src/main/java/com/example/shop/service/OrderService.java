package com.example.shop.service;

import com.example.shop.dto.OrderDTO;
import com.example.shop.dto.request.OrderInfoRequest;
import com.example.shop.dto.request.OrderRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Order;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    ResponseEntity<ApiResponse<String>> createOrder(OrderRequest orderRequest);
    ResponseEntity<ApiResponse<List<OrderDTO>>> getOrderByUserId(Long userId);
    ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders();
    Order saveOrUpdateOrder(Order order);
    ResponseEntity<ApiResponse<String>> updateOrderContactInfo(OrderInfoRequest orderInfoRequest);
    ResponseEntity<ApiResponse<String>> updateOrderStatus(Long orderId, String orderStatus);
    ResponseEntity<ApiResponse<String>> cancelOrder(Long orderId);
}
