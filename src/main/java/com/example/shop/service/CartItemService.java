package com.example.shop.service;

import com.example.shop.model.*;
import com.example.shop.model.cart.CartItem;
import com.example.shop.model.cart.CartItemDTO;
import com.example.shop.mapper.CartItemMapper;
import com.example.shop.model.cart.CartResponse;
import com.example.shop.model.cart.CartItemRequest;
import com.example.shop.model.product.Product;
import com.example.shop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartItemService {

    @Autowired
    private  CartItemMapper cartItemMapper;
    @Autowired
    private  CartItemRepository cartItemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CartItemService.class);

    public ResponseEntity<ApiResponse<List<CartResponse>>> getAllCartItems(Long userId) {
        logger.info("[getAllCartItems] Lấy giỏ hàng cho userId: {}", userId);
        if(userId == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        List<CartItem> cartItems = cartItemRepository.findByUserEntityId(userId);
        logger.debug("[getAllCartItems] Số lượng sản phẩm trong giỏ hàng: {}", cartItems.size());

        return ResponseHandler.generateResponse(Messages.CART_FETCH_SUCCESS,HttpStatus.OK,cartItemMapper.toCartResponseList(cartItems));
    }


    public ResponseEntity<ApiResponse<String>> addItemToCart(CartItemRequest cartItemRequest) {
        logger.info("[addItemToCart] Thêm sản phẩm vào giỏ: userId={}, bookId={}, quantity={}",
                cartItemRequest.getUserId(), cartItemRequest.getBookId(), cartItemRequest.getQuantity());
        if((cartItemRequest.getUserId() == null) || (cartItemRequest.getQuantity() == 0) || (cartItemRequest.getBookId() == null)){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        Product product = productService.findBookById(cartItemRequest.getBookId());
        if(cartItemRequest.getQuantity() > product.getQuantity()){
            return ResponseHandler.generateResponse(Messages.PRODUCT_QUANTITY_NOT_ENOUGH,HttpStatus.CONFLICT,null);
        };

        Optional<CartItem> cartItemOptional = cartItemRepository.findByUserEntityIdAndProductId(
                cartItemRequest.getUserId(), cartItemRequest.getBookId());

        CartItem cartItem = cartItemOptional.orElseGet(() -> {
            logger.debug("[addItemToCart] Sản phẩm chưa tồn tại trong giỏ. Tiến hành tạo mới.");

            CartItem newCartItem = new CartItem();
            newCartItem.setUserEntity(userService.findUserById(cartItemRequest.getUserId()));
            newCartItem.setProduct(productService.findBookById(cartItemRequest.getBookId()));
            newCartItem.setQuantity(cartItemRequest.getQuantity());
            return newCartItem;
        });

        if (cartItemOptional.isPresent()) {
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
            cartItemRepository.save(cartItem);
            logger.debug("[addItemToCart] Sản phẩm đã có sẵn trong giỏ. Cộng thêm số lượng.");
        }

        cartItemRepository.save(cartItem);
        logger.info("[addItemToCart] Đã lưu CartItem thành công.");
        return ResponseHandler.generateResponse(Messages.CART_ADD_SUCCESS,HttpStatus.OK,null);
    }

    public ResponseEntity<ApiResponse<String>> updateItemToCart(CartItemRequest request) {
        if (request.getUserId() == null || request.getBookId() == null || request.getQuantity() < 0) {
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        logger.info("[updateItemToCart] userId={}, bookId={}, quantity={}",
                request.getUserId(), request.getBookId(), request.getQuantity());

        // Cập nhật Redis
        updateQuantityInRedis(request.getUserId(), request.getBookId(), request.getQuantity());

        logger.info("[updateItemToCart] Cập nhật thành công vào Redis.");
        return ResponseHandler.generateResponse(Messages.CART_UPDATE_QUANTITY_SUCCESS, HttpStatus.OK, null);
    }

    private String getKey(Long userId, Long productId) {
        return "cart:" + userId + ":" + productId;
    }

    private void updateQuantityInRedis(Long userId, Long productId, int quantity) {
        String key = getKey(userId, productId);
        if (quantity <= 0) {
            redisTemplate.delete(key);
        } else {
            redisTemplate.opsForValue().set(key, quantity);
        }
    }

    public Integer getQuantity(Long userId, Long productId) {
        return redisTemplate.opsForValue().get(getKey(userId, productId));
    }

    @Transactional
    public ResponseEntity<ApiResponse<String>> checkoutCart(Long userId){
        Map<Long, Integer> cart = getCartForUser(userId);
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            CartItem item = cartItemRepository
                    .findByUserEntityIdAndProductId(userId, productId)
                    .orElseGet(() -> {
                        CartItem newItem = new CartItem();
                        newItem.setUserEntity(userService.findUserById(userId));
                        newItem.setProduct(productService.findBookById(productId));
                        return newItem;
                    });

            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        // Xóa cache sau khi sync
        for (Long productId : cart.keySet()) {
            redisTemplate.delete(getKey(userId, productId));
        }

        return ResponseHandler.generateResponse("Thanh toán thành công và đồng bộ giỏ hàng.", HttpStatus.OK, null);
    }

    public Map<Long, Integer> getCartForUser(Long userId) {
        Set<String> keys = redisTemplate.keys("cart:" + userId + ":*");
        Map<Long, Integer> cart = new HashMap<>();
        if (keys != null) {
            for (String key : keys) {
                String[] parts = key.split(":");
                Long productId = Long.parseLong(parts[2]);
                Integer qty = redisTemplate.opsForValue().get(key);
                cart.put(productId, qty);
            }
        }
        return cart;
    }

    public ResponseEntity<ApiResponse<String>> deleteItem(Long userId, Long bookId) {
        if(userId == null || bookId == null) {
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        logger.info("[deleteItem] Xoá sản phẩm: userId={}, bookId={}", userId, bookId);

        CartItem item = cartItemRepository.findByUserEntityIdAndProductId(userId, bookId)
                .orElseThrow(() -> {
                    logger.error("[deleteItem]  Không tìm thấy sản phẩm để xoá");
                    return new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
                });

        cartItemRepository.delete(item);
        logger.info("[deleteItem] Xoá sản phẩm thành công.");
        return ResponseHandler.generateResponse(Messages.CART_REMOVE_ITEM_SUCCESS,HttpStatus.OK,null);
    }

    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteAllCartItems(Long userId) {
        if(userId == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        logger.info("[deleteAllCartItems] Xoá toàn bộ giỏ hàng cho userId: {}", userId);

        cartItemRepository.deleteByUserEntityId(userId);
        logger.info("[deleteAllCartItems]  Xoá toàn bộ sản phẩm thành công.");

        return ResponseHandler.generateResponse(Messages.CART_CLEAR_SUCCESS,HttpStatus.OK,null);
    }
}
