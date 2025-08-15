package com.example.shop.model.auth;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Long id;
    private String oldPassowrd;
    private String newPassowrd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldPassowrd() {
        return oldPassowrd;
    }

    public void setOldPassowrd(String oldPassowrd) {
        this.oldPassowrd = oldPassowrd;
    }

    public String getNewPassowrd() {
        return newPassowrd;
    }

    public void setNewPassowrd(String newPassowrd) {
        this.newPassowrd = newPassowrd;
    }
}
