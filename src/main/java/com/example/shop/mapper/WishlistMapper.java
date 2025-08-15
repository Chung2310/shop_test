package com.example.shop.mapper;

import com.example.shop.model.wishlist.WishlistDTO;
import com.example.shop.model.wishlist.Wishlist;
import com.example.shop.model.wishlist.WishlistRequest;
import com.example.shop.model.wishlist.WishlistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface WishlistMapper {
    WishlistDTO toDto(Wishlist wishlist);
    Wishlist toEntity(WishlistDTO wishlistDTO);

    List<WishlistDTO> toDtoList(List<Wishlist> wishlists);
    List<Wishlist> toEntityList(List<WishlistDTO> wishlistDTOS);

    Wishlist toEntityWishlist(WishlistRequest wishlistRequest);

    WishlistResponse toResponse(Wishlist wishlist);
    List<WishlistResponse> toResponseList(List<Wishlist> wishlists);
}
