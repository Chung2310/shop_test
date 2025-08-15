package com.example.shop.repository;

import com.example.shop.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);
    UserEntity findUserById(Long id);
    UserEntity findUserByFullNameContainingIgnoreCase(String username);
}
