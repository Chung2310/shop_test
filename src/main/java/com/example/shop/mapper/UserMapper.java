package com.example.shop.mapper;

import com.example.shop.model.user.UserEntityDTO;
import com.example.shop.model.user.UserSellerDTO;
import com.example.shop.model.user.UserEntity;
import com.example.shop.model.user.UserResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntityDTO toDto(UserEntity userEntity);
    UserEntity toEntity(UserEntityDTO userEntityDTO);

    List<UserEntityDTO> toDtoList(List<UserEntity> userEntities);
    List<UserEntity> toEntityList(List<UserEntityDTO> userEntityDTOS);

    UserSellerDTO toSellerDto(UserEntity userEntity);

    UserResponse toUserResponse(UserEntity userEntity);
}
