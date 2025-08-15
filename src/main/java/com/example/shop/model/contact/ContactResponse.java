package com.example.shop.model.contact;

import com.example.shop.model.user.UserEntity;
import com.example.shop.model.user.UserResponse;

import java.time.LocalDateTime;

public class ContactResponse {
    private Long id;
    private UserResponse userEntity;
    private UserResponse userEntityContact;
    private String message;
    private LocalDateTime lastTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponse getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserResponse userEntity) {
        this.userEntity = userEntity;
    }

    public UserResponse getUserEntityContact() {
        return userEntityContact;
    }

    public void setUserEntityContact(UserResponse userEntityContact) {
        this.userEntityContact = userEntityContact;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LocalDateTime lastTime) {
        this.lastTime = lastTime;
    }
}
