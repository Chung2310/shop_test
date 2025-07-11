package com.example.shop.service;

import com.example.shop.dto.BookDTO;
import com.example.shop.model.ApiReponse;
import com.example.shop.model.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface BookService {

    ResponseEntity<ApiReponse<List<BookDTO>>> getAllBooks();
    ResponseEntity<ApiReponse<List<BookDTO>>> getBookByTitle(String title);
    ResponseEntity<ApiReponse<BookDTO>> getBookById(Long id);
    ResponseEntity<ApiReponse<BookDTO>> createBook(BookDTO bookDTO);
    ResponseEntity<ApiReponse<Book>> updateBook(Book book);
    ResponseEntity<ApiReponse<Book>> deleteBook(Long id);

}
