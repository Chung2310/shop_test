package com.example.shop.dto.mapper;

import com.example.shop.dto.BookDTO;
import com.example.shop.dto.ReviewDTO;
import com.example.shop.model.ReviewBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface ReviewMapper {
    @Mapping(source = "user", target = "userDTO")
    @Mapping(source = "book", target = "bookDTO")
    ReviewDTO toDto(ReviewBook reviewBook);
    ReviewBook  toReview(ReviewDTO reviewDTO);

    List<ReviewDTO> toDtoList(List<ReviewBook> reviewBooks);
    List<ReviewBook> toReviewList(List<ReviewDTO> reviewDTOS);
}
