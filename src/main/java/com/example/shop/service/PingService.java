package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PingService {
    Logger logger = Logger.getLogger(PingService.class.getName());

    public ResponseEntity<ApiResponse<String>> ping() {
        logger.log(Level.INFO, "ping đến server");
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Kết nốt đến server",null));
    }
}
