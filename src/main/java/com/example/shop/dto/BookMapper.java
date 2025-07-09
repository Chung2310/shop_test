package com.example.shop.dto;

import com.example.shop.model.Book;
import lombok.Data;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    // ánh xạ 1 đối tượng
    BookDTO toDto(Book book);
    Book toEntity(BookDTO dto);

    // ánh xạ danh sách
    List<BookDTO> toDtoList(List<Book> books);
    List<Book> toEntityList(List<BookDTO> dtos);
}
