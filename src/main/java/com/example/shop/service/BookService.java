package com.example.shop.service;

import com.example.shop.dto.BookDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface BookService {

    ResponseEntity<ApiResponse<List<BookDTO>>> getAllBooks();
    ResponseEntity<ApiResponse<List<BookDTO>>> getBookByTitle(String title);
    ResponseEntity<ApiResponse<BookDTO>> getBookById(Long id);
    ResponseEntity<ApiResponse<BookDTO>> createBook(BookDTO bookDTO);
    ResponseEntity<ApiResponse<Book>> updateBook(Book book);
    ResponseEntity<ApiResponse<Book>> deleteBook(Long id);

}
