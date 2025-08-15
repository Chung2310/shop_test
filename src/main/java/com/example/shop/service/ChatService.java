package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.contact.ChatMessage;
import com.example.shop.model.Messages;
import com.example.shop.model.ResponseHandler;
import com.example.shop.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ResponseEntity<ApiResponse<String>> saveMessage(ChatMessage chatMessage) {

        if(chatMessage == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        chatMessage.setTimestamp(LocalDateTime.now());
        ChatMessage chatMessage1 = chatMessageRepository.save(chatMessage);

        if (chatMessage1 != null) {
            return ResponseHandler.generateResponse(Messages.CHAT_MESSAGE_SENT_SUCCESS,HttpStatus.OK,null);

        }
        else {
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
    }

    public ResponseEntity<ApiResponse<List<ChatMessage>>> getMessagesHistory(Long senderId, Long receiverId) {

        if (senderId == null && receiverId == null) {
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderIdAndReceiverId(senderId, receiverId);

        if (chatMessages.size() > 0) {
            return ResponseHandler.generateResponse(Messages.CHAT_HISTORY_FETCH_SUCCESS,HttpStatus.OK,null);
        }
        else {
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }

    }
}
