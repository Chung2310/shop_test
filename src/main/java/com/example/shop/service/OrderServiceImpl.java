package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.Orders;
import com.example.shop.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public ResponseEntity<ApiResponse<Orders>> saveOrder(Orders order) {
        log.info("[saveOrder] Đang tạo đơn hàng cho userId: {}",order.getUser().getId());

        if(order.getUser().getId() == null) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.NOT_FOUND.value(),"Người dùng không tồn tại!",null));
        }
        else {

        }

        return null;
    }

    @Override
    public Optional<Orders> getOrderById(Long orderId) {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<ApiResponse<List<Orders>>> getAllOrders() {
        return null;
    }
}
