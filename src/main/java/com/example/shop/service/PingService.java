package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.Messages;
import com.example.shop.model.ResponseHandler;
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
        return ResponseHandler.generateResponse(Messages.CONNECT_SERVER_SUCCESS,HttpStatus.OK, null);
    }
}
