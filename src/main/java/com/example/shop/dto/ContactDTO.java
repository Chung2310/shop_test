package com.example.shop.dto;

import java.time.LocalDateTime;

public class ContactDTO {
    private Long id;
    private UserDTO userDTO;
    private UserDTO userContactDTO;
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

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getUserContactDTO() {
        return userContactDTO;
    }

    public void setUserContactDTO(UserDTO userContactDTO) {
        this.userContactDTO = userContactDTO;
    }
}
