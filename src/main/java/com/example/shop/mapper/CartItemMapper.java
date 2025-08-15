package com.example.shop.mapper;

import com.example.shop.model.cart.CartItemDTO;
import com.example.shop.model.cart.CartItem;
import com.example.shop.model.cart.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CartItemMapper {

    CartItemDTO toDTO(CartItem cartItem);
    CartItem toEntity(CartItemDTO cartItemDTO);

    List<CartItemDTO> toDTOList(List<CartItem> cartItemList);
    List<CartItem> toEntityList(List<CartItemDTO> cartItemDTOList);

    @Mapping(source = "userEntity",target = "user")
    CartResponse toCartResponse(CartItem cartItem);
    List<CartResponse> toCartResponseList(List<CartItem> cartItemList);
}
