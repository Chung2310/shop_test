package com.example.shop.service;

import com.example.shop.dto.ProductDTO;

import com.example.shop.dto.mapper.ProductMapper;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductMapper productMapper;

    public Product findBookById(Long id) {
        if(id == null){
            return null;
        }
        return productRepository.findById(id).orElse(null);
    }

    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllBooks(int page, int size) {
        logger.info("[getAllBooks] Đang lấy toàn bộ danh sách sách");

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> booksPage = productRepository.findByIsDeletedFalse(pageable);


        List<ProductDTO> productDTOS = productMapper.toDtoList(booksPage.getContent());

        logger.debug("[getAllBooks] Số lượng sách lấy được: {}", productDTOS.size());

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu thành công!", productDTOS));
    }


    public ResponseEntity<ApiResponse<List<ProductDTO>>> getBookByTitle(String title) {
        logger.info("[getBookByTitle] Tìm sách với tiêu đề chứa: '{}'", title);

        if(title == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                            "Title trống!",null));
        }

        List<Product> products = productRepository.findByTitleContainingIgnoreCase(title);
        List<ProductDTO> productDTOList =  productMapper.toDtoList(products);
        logger.debug("[getBookByTitle] Số sách tìm thấy: {}", products.size());

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu theo title thành công!", productDTOList));
    }


    public ResponseEntity<ApiResponse<ProductDTO>> getBookById(Long id) {
        logger.info("[getBookById] Tìm sách theo ID: {}", id);
        if(id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "id trống!",null)
            );
        }

        Optional<Product> book = productRepository.findById(id);
        if (book.isPresent()) {
            logger.debug("[getBookById] Đã tìm thấy sách: {}", book.get());
            ProductDTO productDTO = productMapper.toDto(book.get());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu theo id thành công!", productDTO));
        } else {
            logger.warn("[getBookById] Không tìm thấy sách với ID: {}", id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.NO_CONTENT.value(), "Không tìm thấy sách", null));
        }
    }

    //admin
    public ResponseEntity<ApiResponse<ProductDTO>> createBook(ProductDTO productDTO) {
        logger.info("[createBook] Tạo sách mới: {}", productDTO);
        if(productDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                            "Thông tin sách bị trống!",null)
            );
        }

        Product product = productMapper.toEntity(productDTO);
        product.setCreatedDate(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        if (savedProduct.getId() != null) {
            logger.debug("[createBook] Đã tạo sách thành công với ID: {}", savedProduct.getId());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Tạo thành công", savedProduct));
        } else {
            logger.error("[createBook] Tạo sách thất bại");
            return ResponseEntity.ok(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Tạo không thành công", null));
        }
    }

    //admin
    public ResponseEntity<ApiResponse<String>> updateBook(Product product) {
        logger.info("[updateBook] Cập nhật sách: {}", product);
        if(product == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                            "Thông tin cập nhập sách trống!",null)
            );
        }

        Optional<Product> findedBook = productRepository.findById(product.getId());
        if (findedBook.isPresent()) {
            product.setUpdatedDate(LocalDateTime.now());
            productRepository.save(product);
            logger.debug("[updateBook] Cập nhật sách thành công");
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Cập nhật thành công", product));
        } else {
            logger.warn("[updateBook] Không tìm thấy sách với ID: {}", product.getId());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.NO_CONTENT.value(), "Không tìm thấy dữ liệu sách", null));
        }
    }

    //admin
    public ResponseEntity<ApiResponse<String>> deleteBook(Long id) {
        logger.info("[deleteBook] Xoá mềm sách với ID: {}", id);
        if(id ==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                            "id sách trống!",null)
            );
        }

        Optional<Product> optionalBook = productRepository.findById(id);
        if (optionalBook.isPresent()) {
            Product product = optionalBook.get();
            product.setDeleted(true);
            productRepository.save(product);
            logger.debug("[deleteBook] Đã xoá mềm sách thành công");
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Xoá thành công!", null));
        } else {
            logger.warn("[deleteBook] Không tìm thấy sách để xoá với ID: {}", id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.NO_CONTENT.value(), "Không tìm thấy sách", null));
        }
    }

    public ResponseEntity<ApiResponse<List<Product>>> getBooksAdmin(){
        List<Product> products = productRepository.findAll();
        if(products == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Lỗi hệ thống không thể truy xuất dữ liệu!",null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(),"Admin lấy thông tin sản phẩm thành công!", products));
    }
}
