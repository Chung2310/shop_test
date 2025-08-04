package com.example.shop.service;

import com.example.shop.dto.ContactDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Contact;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactService {
    ResponseEntity<ApiResponse<String>> createContact(ContactDTO contactDTO);
    ResponseEntity<ApiResponse<List<ContactDTO>>> getListContractDTO(Long userId);
    ResponseEntity<ApiResponse<String>> deleteContact(Long userId, Long userContactId);
}
