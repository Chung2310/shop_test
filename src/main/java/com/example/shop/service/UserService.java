package com.example.shop.service;

import com.example.shop.dto.UserDTO;
import com.example.shop.model.ApiReponse;
import com.example.shop.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService  {

     ResponseEntity<ApiReponse<UserDTO>> createUser(User user);

}
