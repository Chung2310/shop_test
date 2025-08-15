package com.example.shop.mapper;

import com.example.shop.model.review.ReviewDTO;
import com.example.shop.model.review.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface ReviewMapper {
    @Mapping(source = "user", target = "userDTO")
    @Mapping(source = "product", target = "productDTO")
    ReviewDTO toDto(Review review);
    Review toReview(ReviewDTO reviewDTO);

    List<ReviewDTO> toDtoList(List<Review> reviews);
    List<Review> toReviewList(List<ReviewDTO> reviewDTOS);
}
