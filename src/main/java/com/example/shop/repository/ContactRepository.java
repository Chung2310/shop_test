package com.example.shop.repository;

import com.example.shop.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findContactsByUserId(Long userId);
    void deleteByUserIdAndUserContactId(Long userId, Long userContactId);
}
