package com.example.shop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookDTO {
    private String title;
    private String author;
    private String publisher;
    private LocalDateTime publishedDate;
    private BigDecimal price;
    private int quantity;
    private String description_book;
    private String genre;
    private String language;
    private String imageUrl;
}
