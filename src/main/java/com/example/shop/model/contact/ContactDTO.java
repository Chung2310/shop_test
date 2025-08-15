package com.example.shop.model.contact;

import com.example.shop.model.user.UserEntityDTO;

import java.time.LocalDateTime;

public class ContactDTO {
    private Long id;
    private UserEntityDTO userEntityDTO;
    private UserEntityDTO userEntityContactDTO;
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

    public UserEntityDTO getUserEntityDTO() {
        return userEntityDTO;
    }

    public void setUserEntityDTO(UserEntityDTO userEntityDTO) {
        this.userEntityDTO = userEntityDTO;
    }

    public UserEntityDTO getUserEntityContactDTO() {
        return userEntityContactDTO;
    }

    public void setUserEntityContactDTO(UserEntityDTO userEntityContactDTO) {
        this.userEntityContactDTO = userEntityContactDTO;
    }
}
