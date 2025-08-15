package com.example.shop.model.contact;

import com.example.shop.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người dùng hiện tại
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    // Người đã từng liên hệ với user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_contact_id")
    private UserEntity userEntityContact;

    private String lastMessage;

    private LocalDateTime lastTime;

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LocalDateTime lastTime) {
        this.lastTime = lastTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntityContact() {
        return userEntityContact;
    }

    public void setUserEntityContact(UserEntity userEntityContact) {
        this.userEntityContact = userEntityContact;
    }
}
