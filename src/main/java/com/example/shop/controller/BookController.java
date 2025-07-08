package com.example.shop.controller;

import com.example.shop.model.ApiReponse;
import com.example.shop.model.Book;
import com.example.shop.service.BookService;
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
    public ResponseEntity<ApiReponse<List<Book>>> getAll(){
        List<Book> books = bookService.getAll();
        if(books.isEmpty()){
            return ResponseEntity.ok(new ApiReponse<>(404,"Lấy dữ liệu thất bai!",null));
        }
        return ResponseEntity.ok(new ApiReponse<>(200,"Lấy thành công danh sách",books));
    }

    @GetMapping("/title")
    public ResponseEntity<ApiReponse<List<Book>>> getByTitle(@RequestParam String title){
        List<Book> books = bookService.getAll();
        ApiReponse<List<Book>> response = new ApiReponse<>(200, "Success", books);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiReponse<Book>> findById(@PathVariable Long id){
        Book book = bookService.findById(id);
        if(book == null){
            return ResponseEntity.ok(new ApiReponse<>(405,"Không tìm thấy id" + id,null));
        }
        else {
            return ResponseEntity.ok(new ApiReponse<>(200,"Lấy dữ liệu theo id thành công!",book));
        }
    }

    @PostMapping
    public ResponseEntity<ApiReponse<Book>> addBook(@RequestBody Book book){
        bookService.addBook(book);
        return ResponseEntity.ok(new ApiReponse<>(200,"Thêm thành công!",book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiReponse<Book>> updateBook(@PathVariable Long id,@RequestBody Book book){
        bookService.updateBook(id,book);
        return ResponseEntity.ok(new ApiReponse<>(200,"Sửa thành công!",book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiReponse<Book>> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiReponse<>(200,"Xoá thành công",null));
    }
}
