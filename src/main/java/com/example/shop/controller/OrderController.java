package com.example.shop.controller;

import com.example.shop.model.order.OrderDTO;
import com.example.shop.model.order.OrderInfoRequest;
import com.example.shop.model.order.OrderRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.order.OrderResponse;
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
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUserId(@PathVariable("userId") Long userId) {
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

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<String>> cancelOrder(@PathVariable("orderId")  Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<ApiResponse<String>> confirmOrder(@PathVariable("orderId")  Long orderId) {
        return orderService.confirmOrder(orderId);
    }
}
