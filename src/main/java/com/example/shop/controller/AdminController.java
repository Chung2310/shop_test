package com.example.shop.controller;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.Product;
import com.example.shop.model.Order;
import com.example.shop.model.User;
import com.example.shop.service.AdminService;
import com.example.shop.service.ProductService;
import com.example.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService bookService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUser() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        return adminService.deleteUser(id);
    }

    @PutMapping("/user/role/{id}")
    public ResponseEntity<ApiResponse<String>> setRoleAdmin(@PathVariable Long id) {
        return adminService.setRoleAdmin(id);
    }

    @GetMapping("/product")
    public ResponseEntity<ApiResponse<List<Product>>> getBooks() {
        return bookService.getBooksAdmin();
    }

    @GetMapping("/order")
    public ResponseEntity<ApiResponse<List<Order>>> getOrders() {
        return orderService.getOrders();
    }
}
