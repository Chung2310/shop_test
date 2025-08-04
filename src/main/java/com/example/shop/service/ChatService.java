package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.ChatMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChatService {
    ResponseEntity<ApiResponse<String>> saveMessage(ChatMessage chatMessage);
    ResponseEntity<ApiResponse<List<ChatMessage>>> getMessagesHistory(Long senderId, Long receiverId);
}
