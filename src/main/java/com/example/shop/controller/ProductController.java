package com.example.shop.controller;

import com.example.shop.dto.ProductDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private ProductService bookServiceImpl;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return bookServiceImpl.getAllBooks(page, size);
    }

    @GetMapping("/title")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getBookByTitle(@RequestParam String title){
        return bookServiceImpl.getBookByTitle(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getBookById(@PathVariable Long id){
        return bookServiceImpl.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createBook(@Valid @RequestBody ProductDTO book){
        return bookServiceImpl.createBook(book);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateBook( @RequestBody Product product){
        return bookServiceImpl.updateBook(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id){
        return bookServiceImpl.deleteBook(id);
    }
}
