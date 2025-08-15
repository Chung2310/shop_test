package com.example.shop.service;

import com.example.shop.model.Messages;
import com.example.shop.model.ResponseHandler;
import com.example.shop.model.wishlist.WishlistDTO;
import com.example.shop.mapper.WishlistMapper;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.wishlist.Wishlist;
import com.example.shop.model.wishlist.WishlistRequest;
import com.example.shop.model.wishlist.WishlistResponse;
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

    public ResponseEntity<ApiResponse<List<WishlistResponse>>> getWishlistsByUserId(Long userId) {
        logger.info("Yêu cầu lấy danh sách yêu thích cho userId={}", userId);

        if (userId == null) {
            logger.warn("userId bị null");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        List<Wishlist> wishlists = wishlistRepository.findByUserEntityId(userId);
        if (wishlists.isEmpty()) {
            logger.warn("Không tìm thấy wishlist nào cho userId={}", userId);
            return ResponseHandler.generateResponse(Messages.WISH_LIST_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        logger.info("Tìm thấy {} wishlist cho userId={}", wishlists.size(), userId);
        return ResponseHandler.generateResponse(Messages.WISHLIST_FETCH_SUCCESS,HttpStatus.OK, wishlistMapper.toResponseList(wishlists));
    }

    public ResponseEntity<ApiResponse<WishlistDTO>> addToWishlist(WishlistRequest wishlistRequest) {
        logger.info("Yêu cầu thêm wishlist: {}", wishlistRequest);

        if (wishlistRequest.getBookId() == null || wishlistRequest.getUserId() == null) {
            logger.warn("Thiếu bookId hoặc userId trong wishlistDTO: {}", wishlistRequest);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        Wishlist wishlist = wishlistMapper.toEntityWishlist(wishlistRequest);
        wishlist = wishlistRepository.save(wishlist);
        logger.info("Đã thêm wishlist: {}", wishlist);

        return ResponseHandler.generateResponse(Messages.WISHLIST_ADD_SUCCESS,HttpStatus.CREATED, null);
    }

    public ResponseEntity<ApiResponse<WishlistDTO>> removeFromWishlist(WishlistRequest wishlistRequest) {
        logger.info("Yêu cầu xoá wishlist: {}", wishlistRequest);

        if (wishlistRequest.getBookId() == null || wishlistRequest.getUserId() == null) {
            logger.warn("Thiếu bookId hoặc userId trong wishlistDTO: {}", wishlistRequest);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        Wishlist wishlist = wishlistMapper.toEntityWishlist(wishlistRequest);
        logger.info("Đã tìm thấy wishlist để xoá: {}", wishlist);

        wishlistRepository.delete(wishlist);
        logger.info("Đã xoá wishlist thành công: {}", wishlist);

        return ResponseHandler.generateResponse(Messages.WISHLIST_REMOVE_SUCCESS,HttpStatus.OK, null);
    }
}
