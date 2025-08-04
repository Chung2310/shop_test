package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.ChatMessage;
import com.example.shop.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    public ResponseEntity<ApiResponse<String>> saveMessage(ChatMessage chatMessage) {

        if(chatMessage == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new  ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!",null)
            );
        }

        chatMessage.setTimestamp(LocalDateTime.now());
        ChatMessage chatMessage1 = chatMessageRepository.save(chatMessage);

        if (chatMessage1 != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(HttpStatus.CREATED.value(),"Lưu tin nhắn thành công!",null)
            );
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Lỗi hệ thống không thể lưu tin nhắn!",null)
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponse<List<ChatMessage>>> getMessagesHistory(Long senderId, Long receiverId) {

        if (senderId == null && receiverId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!",null)
            );
        }

        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderIdAndReceiverId(senderId, receiverId);

        if (chatMessages.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách lịch sử thành công!", chatMessages)
            );
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),"Lỗi hệ thống!. Không thế lấy dữ liệu!", null)
            );
        }

    }
}
