package com.example.shop.controller;

import com.example.shop.dto.BookDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Book;
import com.example.shop.service.BookServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/book")
@RequiredArgsConstructor
public class BookController {

    @Autowired
    private  BookServiceImpl bookServiceImpl;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookDTO>>> getAllBooks(){
        return bookServiceImpl.getAllBooks();
    }

    @GetMapping("/title")
    public ResponseEntity<ApiResponse<List<BookDTO>>> getBookByTitle(@RequestParam String title){
        return bookServiceImpl.getBookByTitle(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDTO>> getBookById(@PathVariable Long id){
        return bookServiceImpl.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookDTO>> createBook(@Valid @RequestBody BookDTO book){
        return bookServiceImpl.createBook(book);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Book>> updateBook( @RequestBody Book book){
        return bookServiceImpl.updateBook(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> deleteBook(@PathVariable Long id){
        return bookServiceImpl.deleteBook(id);
    }
}
