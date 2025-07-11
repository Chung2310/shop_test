package com.example.shop.dto;

import com.example.shop.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO  toDto(User user);
    User toEntity(UserDTO userDTO);

    List<UserDTO> toDtoList(List<User> users);
    List<User> toEntityList(List<UserDTO> userDTOS);
}
