package com.example.shop.repository;

import com.example.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<User, Long> {
}
