package com.example.shop.controller;

import com.example.shop.model.contact.ContactDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.contact.ContactRequest;
import com.example.shop.model.contact.ContactResponse;
import com.example.shop.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<ContactResponse>>> getContactsByUserId(@PathVariable Long userId) {
        return contactService.getListContractDTO(userId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createContact(@RequestBody ContactRequest contact) {
        return contactService.createContact(contact);
    }

    @DeleteMapping("/{userId}/{userContactId}")
    public ResponseEntity<ApiResponse<String>> deleteContact(@PathVariable Long userContactId, @PathVariable Long userId) {
        return contactService.deleteContact(userId,userContactId);
    }
}
