package com.example.shop.dto.mapper;

import com.example.shop.dto.UserDTO;
import com.example.shop.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);

    List<UserDTO> toDtoList(List<User> users);
    List<User> toEntityList(List<UserDTO> userDTOS);
}
