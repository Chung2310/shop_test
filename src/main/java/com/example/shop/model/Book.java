package com.example.shop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Không được để trống tác giả")
    private String author;

    private boolean deleted = false;

    @NotBlank(message = "Không được để trống Nhà cung cấp")
    private String publisher;

    @NotNull(message = "Không được để trống ngày xuất bản")
    @PastOrPresent(message = "Ngày xuất bản không được vượt quá hiện tại")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime publishedDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Giá không được âm")
    private BigDecimal price;

    @Min(value = 0, message = "Số lương không được phép âm")
    private int quantity;

    @NotBlank(message = "Không được để trống thể loại")
    private String genre;

    @NotBlank(message = "Không được để trống ngôn ngữ")
    private String language;

    @NotBlank(message = "Không được để trống mô tả")
    @Column(name = "description_book")
    private String description_book;

    @NotBlank(message = "Không được để trống link ảnh")
    private String imageUrl;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime updatedDate;
}
