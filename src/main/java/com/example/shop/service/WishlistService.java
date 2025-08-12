package com.example.shop.service;

import com.example.shop.dto.WishlistDTO;
import com.example.shop.dto.mapper.WishlistMapper;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.Wishlist;
import com.example.shop.repository.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {
    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WishlistMapper wishlistMapper;

    public ResponseEntity<ApiResponse<List<WishlistDTO>>> getWishlistsByUserId(Long userId) {
        logger.info("Yêu cầu lấy danh sách yêu thích cho userId={}", userId);

        if (userId == null) {
            logger.warn("userId bị null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!", null)
            );
        }

        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        if (wishlists.isEmpty()) {
            logger.warn("Không tìm thấy wishlist nào cho userId={}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Lỗi truy vấn dữ liệu!", null)
            );
        }

        logger.info("Tìm thấy {} wishlist cho userId={}", wishlists.size(), userId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Lấy danh sách yêu thích thành công!", wishlistMapper.toDtoList(wishlists))
        );
    }

    public ResponseEntity<ApiResponse<WishlistDTO>> addToWishlist(WishlistDTO wishlistDTO) {
        logger.info("Yêu cầu thêm wishlist: {}", wishlistDTO);

        if (wishlistDTO.getBookId() == null || wishlistDTO.getUserId() == null) {
            logger.warn("Thiếu bookId hoặc userId trong wishlistDTO: {}", wishlistDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!", null)
            );
        }

        Wishlist wishlist = wishlistMapper.toEntity(wishlistDTO);
        wishlist = wishlistRepository.save(wishlist);
        logger.info("Đã thêm wishlist: {}", wishlist);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Thêm vào danh sách yêu thích thành công!", wishlistMapper.toDto(wishlist))
        );
    }

    public ResponseEntity<ApiResponse<WishlistDTO>> removeFromWishlist(WishlistDTO wishlistDTO) {
        logger.info("Yêu cầu xoá wishlist: {}", wishlistDTO);

        if (wishlistDTO.getBookId() == null || wishlistDTO.getUserId() == null) {
            logger.warn("Thiếu bookId hoặc userId trong wishlistDTO: {}", wishlistDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu!", null)
            );
        }

        Wishlist wishlist = wishlistMapper.toEntity(wishlistDTO);
        logger.info("Đã tìm thấy wishlist để xoá: {}", wishlist);

        wishlistRepository.delete(wishlist);
        logger.info("Đã xoá wishlist thành công: {}", wishlist);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Xoá khỏi danh sách yêu thích thành công!", null)
        );
    }
}
