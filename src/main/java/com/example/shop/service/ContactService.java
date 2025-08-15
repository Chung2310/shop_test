package com.example.shop.service;

import com.example.shop.model.Messages;
import com.example.shop.model.ResponseHandler;
import com.example.shop.model.contact.ContactDTO;
import com.example.shop.mapper.ContactMapper;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.contact.Contact;
import com.example.shop.model.contact.ContactRequest;
import com.example.shop.model.contact.ContactResponse;
import com.example.shop.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;

    public ResponseEntity<ApiResponse<String>> createContact(ContactRequest contactRequest) {
        if(contactRequest == null) {
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        Contact contact = contactMapper.toContact(contactRequest);
        contact.setLastTime(LocalDateTime.now());
        contactRepository.save(contact);

        if(contact == null) {
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        return ResponseHandler.generateResponse(Messages.CONTACT_CREATED_SUCCESS,HttpStatus.CREATED,null);
    }

    public ResponseEntity<ApiResponse<List<ContactResponse>>> getListContractDTO(Long userId) {
        if(userId == null) {
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        List<Contact> contacts = contactRepository.findContactsByUserEntityId(userId);

        if(contacts.isEmpty()) {
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
        return ResponseHandler.generateResponse(Messages.CHAT_HISTORY_FETCH_SUCCESS,HttpStatus.OK,contactMapper.toContactResponseList(contacts));
    }

    public ResponseEntity<ApiResponse<String>> deleteContact(Long userId, Long userContactId) {
        if (userId == null || userContactId == null) {
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        contactRepository.deleteByUserEntityIdAndUserEntityContactId(userId, userContactId);
        return ResponseHandler.generateResponse(Messages.CONTACT_DELETED_SUCCESS,HttpStatus.OK,null);
    }
}
