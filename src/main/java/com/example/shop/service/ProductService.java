package com.example.shop.service;

import com.example.shop.model.product.ProductDTO;

import com.example.shop.mapper.ProductMapper;
import com.example.shop.model.*;
import com.example.shop.model.product.Product;
import com.example.shop.model.product.ProductRequest;
import com.example.shop.model.product.ProductResponse;
import com.example.shop.model.user.UserEntity;
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

import java.math.BigDecimal;
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

    @Autowired
    private UserService userService;

    public Product findBookById(Long id) {
        if(id == null){
            return null;
        }
        return productRepository.findById(id).orElse(null);
    }

    public Product createOrUpdateProduct(String mode,ProductRequest productRequest){
        if ((productRequest.getId() == 0)
                || (productRequest.getSellerId() == 0)
                || (productRequest.getTitle() == null)
                || (productRequest.getAuthor() == null)
                || (productRequest.getPublishedDate() == null)
                || (productRequest.getPrice().compareTo(BigDecimal.ZERO) == 0)
                || (productRequest.getQuantity() == 0)
                || (productRequest.getGenre() == null)
                || (productRequest.getLanguage() == null)
                || (productRequest.getDescription() == null)
                || (productRequest.getImages() == null)) {
            return null;
        }
        UserEntity userEntity = userService.findUserById(productRequest.getSellerId());
        if (userEntity == null){
            return null;
        }

        Product product = productMapper.toProductEntity(productRequest);
        product.setSeller(userEntity);
        if(mode.equals("CREATE")){
            product.setCreatedDate(LocalDateTime.now());
        }
        else {
            product.setUpdatedDate(LocalDateTime.now());
        }
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    public Product createOrUpdateProductDTO(Product product){
        return productRepository.save(product);
    }

    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(int page, int size) {
        logger.info("[getAllProducts] Đang lấy toàn bộ danh sách sản phẩm");

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> booksPage = productRepository.findByIsDeletedFalse(pageable);


        List<ProductResponse> productResponseList = productMapper.toProductResponseList(booksPage.getContent());

        logger.debug("[getAllProducts] Số lượng sản phẩm lấy được: {}", productResponseList.size());

        return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS, HttpStatus.OK, productResponseList);
    }


    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductByTitle(String title) {
        logger.info("[getProductByTitle] Tìm sản phẩm với tiêu đề chứa: '{}'", title);

        if(title == null)
        {
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        List<Product> products = productRepository.findByTitleContainingIgnoreCase(title);
        List<ProductResponse> productResponseList =  productMapper.toProductResponseList(products);
        logger.debug("[getProductByTitle] Số sản phẩm tìm thấy: {}", products.size());

        return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS, HttpStatus.OK, productResponseList);
    }


    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(Long id) {
        logger.info("[getProductById] Tìm sản phẩm theo ID: {}", id);
        if(id == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        Optional<Product> book = productRepository.findById(id);
        if (book.isPresent()) {
            logger.debug("[getProductById] Đã tìm thấy sản phẩm: {}", book.get());
            ProductResponse productResponse = productMapper.toProductResponse(book.get());
            return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS,HttpStatus.OK,productResponse);
        } else {
            logger.warn("[getProductById] Không tìm thấy sản phẩm với ID: {}", id);
            return ResponseHandler.generateResponse(Messages.PRODUCT_NOT_FOUND,HttpStatus.NOT_FOUND,null);
        }
    }

    public ResponseEntity<ApiResponse<String>> createProduct(ProductRequest productRequest) {
        logger.info("[createProduct] Tạo sản phẩm mới: {}", productRequest);
        if(productRequest == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        Product product = createOrUpdateProduct("CREATE", productRequest);

        if (product != null) {
            logger.debug("[createProduct] Đã tạo sản phẩm thành công với ID: {}", product.getId());
            return ResponseHandler.generateResponse(Messages.PRODUCT_CREATED,HttpStatus.CREATED,null);
        } else {
            logger.error("[createProduct] Tạo sản phẩm thất bại");
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
    }

    //admin
    public ResponseEntity<ApiResponse<String>> updateProduct(ProductRequest productRequest) {
        Long id = productRequest.getId();
        logger.info("[updateProduct] Cập nhật sản phẩm: {}", productRequest.getId());

        Product product = createOrUpdateProduct("UPDATE", productRequest);

        if (product != null) {
            logger.debug("[updateProduct] Cập nhật sản phẩm thành công");
            return ResponseHandler.generateResponse(Messages.PRODUCT_UPDATED,HttpStatus.OK,null);
        } else {
            logger.warn("[updateProduct] Không tìm thấy sản phẩm với ID: {}", id);
            return ResponseHandler.generateResponse(Messages.PRODUCT_NOT_FOUND,HttpStatus.NOT_FOUND,null);
        }
    }

    //admin
    public ResponseEntity<ApiResponse<String>> deleteProduct(Long id) {
        logger.info("[deleteProduct] Xoá mềm sản phẩm với ID: {}", id);
        if(id ==null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        Optional<Product> optionalBook = productRepository.findById(id);
        if (optionalBook.isPresent()) {
            Product product = optionalBook.get();
            product.setDeleted(true);
            productRepository.save(product);
            logger.debug("[deleteProduct] Đã xoá mềm sản phẩm thành công");
            return ResponseHandler.generateResponse(Messages.PRODUCT_DELETED,HttpStatus.OK,null);
        } else {
            logger.warn("[deleteProduct] Không tìm thấy sản phẩm để xoá với ID: {}", id);
            return ResponseHandler.generateResponse(Messages.PRODUCT_NOT_FOUND,HttpStatus.NOT_FOUND,null);
        }
    }

    public ResponseEntity<ApiResponse<List<Product>>> getBooksAdmin() {
        logger.info(" Admin yêu cầu lấy danh sách sản phẩm");

        List<Product> products = productRepository.findAll();

        if (products == null) {
            logger.error(" Không thể truy xuất dữ liệu sản phẩm từ database");
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }

        if (products.isEmpty()) {
            logger.warn("️ Danh sách sản phẩm trả về rỗng");
        } else {
            logger.info(" Lấy danh sách sản phẩm thành công. Số lượng: {}", products.size());
        }

        return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS,HttpStatus.OK,products);
    }
}
