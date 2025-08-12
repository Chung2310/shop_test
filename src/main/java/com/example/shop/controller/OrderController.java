package com.example.shop.controller;

import com.example.shop.dto.OrderDTO;
import com.example.shop.dto.request.OrderInfoRequest;
import com.example.shop.dto.request.OrderRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUserId(@PathVariable("userId") Long userId) {
        return orderService.getOrderByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateOrderContactInfo(@RequestBody OrderInfoRequest orderInfoRequest) {
        return orderService.updateOrderContactInfo(orderInfoRequest);
    }

    @PostMapping("/updateOrderStatus/{orderId}")
    public ResponseEntity<ApiResponse<String>> updateOrderStatus(@PathVariable("orderId") Long orderId,@RequestParam String orderStatus) {
        return orderService.updateOrderStatus(orderId, orderStatus);
    }
}
