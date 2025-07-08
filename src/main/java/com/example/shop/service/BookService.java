package com.example.shop.service;

import com.example.shop.model.ApiReponse;
import com.example.shop.model.Book;
import com.example.shop.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAll(){
        return bookRepository.findAll();
    }

    public List<Book> findByTitle(String title){
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public Book findById(Long id){
        return bookRepository.findById(id).orElse(null);
    }

    public Book addBook(Book book){
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book book){
        return bookRepository.save(book);
    }

    @Transactional
    public Book deleteBook(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với id = " + id));

        bookRepository.delete(book);
        return book;
    }

    public Page<Book> searchBooks(String keyword, int page, int size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

}
