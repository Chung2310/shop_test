package com.example.shop.dto;

public class ContactDTO {
    private Long id;
    private UserDTO userDTO;
    private UserDTO userContactDTO;

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
