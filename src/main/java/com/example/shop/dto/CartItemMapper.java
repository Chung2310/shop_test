package com.example.shop.dto;

import com.example.shop.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemDTO  toDTO(CartItem cartItem);
    CartItem toEntity(CartItemDTO cartItemDTO);

    List<CartItemDTO> toDTOList(List<CartItem> cartItemList);
    List<CartItem> toEntityList(List<CartItemDTO> cartItemDTOList);
}
