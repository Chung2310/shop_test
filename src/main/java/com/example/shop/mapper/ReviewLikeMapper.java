package com.example.shop.mapper;

import com.example.shop.model.review.ReviewLikeDTO;
import com.example.shop.model.review.ReviewLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ReviewMapper.class})
public interface ReviewLikeMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "review.id", target = "reviewId")
    ReviewLikeDTO toDto(ReviewLike dto);


}
