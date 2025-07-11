package com.example.shop.controller;

import com.example.shop.dto.BookDTO;
import com.example.shop.model.ApiReponse;
import com.example.shop.model.Book;
import com.example.shop.service.BookService;
import com.example.shop.service.BookServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookServiceImpl bookServiceImpl;

    @GetMapping
    public ResponseEntity<ApiReponse<List<BookDTO>>> getAllBooks(){
        return bookServiceImpl.getAllBooks();
    }

    @GetMapping("/title")
    public ResponseEntity<ApiReponse<List<BookDTO>>> getBookByTitle(@RequestParam String title){
        return bookServiceImpl.getBookByTitle(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiReponse<BookDTO>> getBookById(@PathVariable Long id){
        return bookServiceImpl.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<ApiReponse<BookDTO>> createBook(@Valid @RequestBody BookDTO book){
        return bookServiceImpl.createBook(book);
    }

    @PutMapping
    public ResponseEntity<ApiReponse<Book>> updateBook( @RequestBody Book book){
        return bookServiceImpl.updateBook(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiReponse<Book>> deleteBook(@PathVariable Long id){
        return bookServiceImpl.deleteBook(id);
    }
}
