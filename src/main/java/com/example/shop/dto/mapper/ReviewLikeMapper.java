package com.example.shop.dto.mapper;

import com.example.shop.dto.ReviewLikeDTO;
import com.example.shop.model.ReviewLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ReviewMapper.class})
public interface ReviewLikeMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "review.id", target = "reviewId")
    ReviewLikeDTO toDto(ReviewLike dto);


}
