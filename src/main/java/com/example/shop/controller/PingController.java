package com.example.shop.controller;

import com.example.shop.model.ApiResponse;
import com.example.shop.service.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ping")
public class PingController {
    @Autowired
    private PingService pingService;

    @GetMapping
    public ResponseEntity<ApiResponse<String>> ping() {
        return pingService.ping();
    }
}
