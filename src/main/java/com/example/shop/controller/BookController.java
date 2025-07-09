package com.example.shop.controller;

import com.example.shop.dto.BookDTO;
import com.example.shop.model.ApiReponse;
import com.example.shop.model.Book;
import com.example.shop.service.BookService;
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
    private final BookService  bookService;

    @GetMapping
    public ResponseEntity<ApiReponse<List<BookDTO>>> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/title")
    public ResponseEntity<ApiReponse<List<BookDTO>>> getBookByTitle(@RequestParam String title){
        return bookService.getBookByTitle(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiReponse<BookDTO>> getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<ApiReponse<BookDTO>> createBook(@Valid @RequestBody BookDTO book){
        return bookService.createBook(book);
    }

    @PutMapping
    public ResponseEntity<ApiReponse<Book>> updateBook( @RequestBody Book book){
        return bookService.updateBook(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiReponse<Book>> deleteBook(@PathVariable Long id){
        return bookService.deleteBook(id);
    }
}
