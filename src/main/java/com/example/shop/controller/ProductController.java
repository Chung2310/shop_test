package com.example.shop.controller;

import com.example.shop.model.product.ProductDTO;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.product.Product;
import com.example.shop.model.product.ProductRequest;
import com.example.shop.model.product.ProductResponse;
import com.example.shop.service.ProductService;
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
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return bookServiceImpl.getAllProducts(page, size);
    }

    @GetMapping("/title")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductByTitle(@RequestParam String title){
        return bookServiceImpl.getProductByTitle(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id){
        return bookServiceImpl.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createProduct( @RequestBody ProductRequest productRequest){
        return bookServiceImpl.createProduct(productRequest);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateProduct( @RequestBody ProductRequest productRequest){
        return bookServiceImpl.updateProduct(productRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id){
        return bookServiceImpl.deleteProduct(id);
    }
}
