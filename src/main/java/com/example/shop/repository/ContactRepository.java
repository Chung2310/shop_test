package com.example.shop.repository;

import com.example.shop.model.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findContactsByUserEntityId(Long userId);
    void deleteByUserEntityIdAndUserEntityContactId(Long userId, Long userContactId);
}
