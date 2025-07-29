package com.example.shop.dto.mapper;

import com.example.shop.dto.WishlistDTO;
import com.example.shop.model.Wishlist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    WishlistDTO toDto(Wishlist wishlist);
    Wishlist toEntity(WishlistDTO wishlistDTO);

    List<WishlistDTO> toDtoList(List<Wishlist> wishlists);
    List<Wishlist> toEntityList(List<WishlistDTO> wishlistDTOS);
}
