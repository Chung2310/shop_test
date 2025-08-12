package com.example.shop.controller;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.ChatMessage;
import com.example.shop.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> saveMessage(@RequestBody ChatMessage chatMessage) {
        return chatService.saveMessage(chatMessage);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatMessage>>> getMessagesHistory(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return chatService.getMessagesHistory(senderId, receiverId);
    }

}
