package com.example.shop.model.auth;

import com.example.shop.model.user.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity userEntity;

    @Column(name = "created_date")
    private LocalDateTime createdAt;

    public PasswordResetToken(UserEntity userEntity, String token, LocalDateTime createdAt) {
        this.userEntity = userEntity;
        this.token = token;
        this.createdAt = createdAt;
    }

    public PasswordResetToken() {

    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
