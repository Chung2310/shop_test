package com.example.shop.service;

import com.example.shop.dto.BookDTO;

import com.example.shop.dto.mapper.BookMapper;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Book;
import com.example.shop.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements  BookService {

    @Autowired
    private  BookRepository bookRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookMapper bookMapper;

    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public ResponseEntity<ApiResponse<List<BookDTO>>> getAllBooks() {
        logger.info("[getAllBooks] Đang lấy toàn bộ danh sách sách");

        List<Book> books = bookRepository.findAll();

        List<BookDTO> bookDTOS = bookMapper.toDtoList(books);

        logger.debug("[getAllBooks] Số lượng sách lấy được: {}", books.size());

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu thành công!", bookDTOS));
    }


    public ResponseEntity<ApiResponse<List<BookDTO>>> getBookByTitle(String title) {
        logger.info("[getBookByTitle] Tìm sách với tiêu đề chứa: '{}'", title);

        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        List<BookDTO> bookDTOList =  bookMapper.toDtoList(books);
        logger.debug("[getBookByTitle] Số sách tìm thấy: {}", books.size());

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu theo title thành công!", bookDTOList));
    }


    public ResponseEntity<ApiResponse<BookDTO>> getBookById(Long id) {
        logger.info("[getBookById] Tìm sách theo ID: {}", id);

        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            logger.debug("[getBookById] Đã tìm thấy sách: {}", book.get());
            BookDTO bookDTO = bookMapper.toDto(book.get());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu theo id thành công!", bookDTO));
        } else {
            logger.warn("[getBookById] Không tìm thấy sách với ID: {}", id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.NO_CONTENT.value(), "Không tìm thấy sách", null));
        }
    }


    public ResponseEntity<ApiResponse<BookDTO>> createBook(BookDTO bookDTO) {
        logger.info("[createBook] Tạo sách mới: {}", bookDTO);
        Book book  = bookMapper.toEntity(bookDTO);
        book.setCreatedDate(LocalDateTime.now());
        Book savedBook = bookRepository.save(book);
        if (savedBook.getId() != null) {
            logger.debug("[createBook] Đã tạo sách thành công với ID: {}", savedBook.getId());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Tạo thành công", savedBook));
        } else {
            logger.error("[createBook] Tạo sách thất bại");
            return ResponseEntity.ok(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Tạo không thành công", null));
        }
    }


    public ResponseEntity<ApiResponse<Book>> updateBook(Book book) {
        logger.info("[updateBook] Cập nhật sách: {}", book);

        Optional<Book> findedBook = bookRepository.findById(book.getId());
        if (findedBook.isPresent()) {
            book.setUpdatedDate(LocalDateTime.now());
            bookRepository.save(book);
            logger.debug("[updateBook] Cập nhật sách thành công");
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Cập nhật thành công", book));
        } else {
            logger.warn("[updateBook] Không tìm thấy sách với ID: {}", book.getId());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.NO_CONTENT.value(), "Không tìm thấy dữ liệu sách", null));
        }
    }


    public ResponseEntity<ApiResponse<Book>> deleteBook(Long id) {
        logger.info("[deleteBook] Xoá mềm sách với ID: {}", id);

        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setDeleted(true);
            bookRepository.save(book);
            logger.debug("[deleteBook] Đã xoá mềm sách thành công");
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Xoá thành công!", null));
        } else {
            logger.warn("[deleteBook] Không tìm thấy sách để xoá với ID: {}", id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.NO_CONTENT.value(), "Không tìm thấy sách", null));
        }
    }
}
