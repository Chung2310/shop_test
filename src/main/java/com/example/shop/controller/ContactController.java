package com.example.shop.controller;

import com.example.shop.dto.ContactDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Contact;
import com.example.shop.service.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactServiceImpl  contactService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<ContactDTO>>> getContactsByUserId(@PathVariable Long userId) {
        return contactService.getListContractDTO(userId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createContact(@RequestBody ContactDTO contact) {
        return contactService.createContact(contact);
    }

    @DeleteMapping("/{userId}/{userContactId}")
    public ResponseEntity<ApiResponse<String>> deleteContact(@PathVariable Long userContactId, @PathVariable Long userId) {
        return contactService.deleteContact(userId,userContactId);
    }
}
