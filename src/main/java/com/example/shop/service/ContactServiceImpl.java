package com.example.shop.service;

import com.example.shop.dto.ContactDTO;
import com.example.shop.dto.mapper.ContactMapper;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Contact;
import com.example.shop.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;

    @Override
    public ResponseEntity<ApiResponse<String>> createContact(ContactDTO contactDTO) {
        if(contactDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!",null)
            );
        }

        Contact contact =  contactRepository.save(contactMapper.toEntity(contactDTO));

        if(contact == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.PROCESSING.value(), "Lỗi hệ thống! Không thể lưu liên hệ!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(HttpStatus.CREATED.value(), "Tạo liên hệ thành công!",null)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<List<ContactDTO>>> getListContractDTO(Long userId) {
        if(userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu",null)
            );
        }

        List<Contact> contacts = contactRepository.findContactsByUserId(userId);

        if(contacts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Lỗi hệ thống! Không thể lấy dữ liệu!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Lấy dữ liệu liên hệ thành công!", contactMapper.toContactDTOs(contacts))
        );
    }

    @Override
    public ResponseEntity<ApiResponse<String>> deleteContact(Long userId, Long userContactId) {
        if (userId == null || userContactId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!",null)
            );
        }

        contactRepository.deleteByUserIdAndUserContactId(userId, userContactId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Xoá liên hệ thành công!", null)
        );
    }
}
