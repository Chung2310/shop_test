package com.example.shop.service;

import com.example.shop.dto.CartItemDTO;
import com.example.shop.dto.mapper.CartItemMapper;
import com.example.shop.dto.request.CartItemRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.CartItem;
import com.example.shop.repository.BookRepository;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private  CartItemMapper cartItemMapper;
    @Autowired
    private  CartItemRepository cartItemRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private BookServiceImpl bookService;

    private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImpl.class);

    @Override
    public ResponseEntity<ApiResponse<List<CartItemDTO>>> getAllCartItems(Long userId) {
        logger.info("[getAllCartItems] Lấy giỏ hàng cho userId: {}", userId);
        if(userId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "userId bị trống!",null)
            );
        }

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        logger.debug("[getAllCartItems] Số lượng sản phẩm trong giỏ hàng: {}", cartItems.size());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Lấy dữ liệu giỏ hàng thành công!", cartItemMapper.toDTOList(cartItems))
        );
    }

    @Override
    public ResponseEntity<ApiResponse<CartItemDTO>> addItemToCart(CartItemRequest cartItemRequest) {
        logger.info("[addItemToCart] Thêm sản phẩm vào giỏ: userId={}, bookId={}, quantity={}",
                cartItemRequest.getUserId(), cartItemRequest.getBookId(), cartItemRequest.getQuantity());
        if((cartItemRequest.getUserId() == null) || (cartItemRequest.getQuantity() == 0) || (cartItemRequest.getBookId() == null)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin đê thêm vào giỏ hàng!",null)
            );
        }

        Optional<CartItem> cartItemOptional = cartItemRepository.findByUserIdAndBookId(
                cartItemRequest.getUserId(), cartItemRequest.getBookId());

        CartItem cartItem = cartItemOptional.orElseGet(() -> {
            logger.debug("[addItemToCart] Sản phẩm chưa tồn tại trong giỏ. Tiến hành tạo mới.");

            CartItem newCartItem = new CartItem();
            newCartItem.setUser(userService.findUserById(cartItemRequest.getUserId()));
            newCartItem.setBook(bookService.findBookById(cartItemRequest.getBookId()));
            newCartItem.setQuantity(cartItemRequest.getQuantity());
            return newCartItem;
        });

        if (cartItemOptional.isPresent()) {
            cartItem.setQuantity(cartItemRequest.getQuantity() + cartItemRequest.getQuantity());
            cartItemRepository.save(cartItem);
            logger.debug("[addItemToCart] Sản phẩm đã có sẵn trong giỏ. Cộng thêm số lượng.");
        }

        cartItemRepository.save(cartItem);
        logger.info("[addItemToCart] ✅ Đã lưu CartItem thành công.");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Thêm vào giỏ hàng thành công!", cartItemMapper.toDTO(cartItem)));
    }

    @Override
    public ResponseEntity<ApiResponse<CartItemDTO>> updateItemToCart(CartItemRequest cartItemRequest) {
        if (cartItemRequest.getBookId() == null || cartItemRequest.getQuantity() == 0 || cartItemRequest.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin để cập nhập item giỏ hàng!", null)
            );
        }

        logger.info("[updateItemToCart] Cập nhật số lượng: userId={}, bookId={}, quantity={}",
                cartItemRequest.getUserId(), cartItemRequest.getBookId(), cartItemRequest.getQuantity());

        CartItem item = cartItemRepository.findByUserIdAndBookId(
                        cartItemRequest.getUserId(), cartItemRequest.getBookId())
                .orElseThrow(() -> {
                    logger.error("[updateItemToCart] ❌ Không tìm thấy sản phẩm để cập nhật");
                    return new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
                });

        item.setQuantity(cartItemRequest.getQuantity());
        cartItemRepository.save(item);

        logger.info("[updateItemToCart] ✅ Cập nhật số lượng thành công.");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cập nhật số lượng thành công!", cartItemMapper.toDTO(item)));
    }

    @Override
    public ResponseEntity<ApiResponse<String>> deleteItem(Long userId, Long bookId) {
        if(userId == null || bookId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin để xoá item giỏ hàng!", null)
            );
        }

        logger.info("[deleteItem] Xoá sản phẩm: userId={}, bookId={}", userId, bookId);

        CartItem item = cartItemRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> {
                    logger.error("[deleteItem] ❌ Không tìm thấy sản phẩm để xoá");
                    return new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
                });

        cartItemRepository.delete(item);
        logger.info("[deleteItem] ✅ Xoá sản phẩm thành công.");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Xoá item khỏi giỏ hàng thành công!", null));
    }

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<String>> deleteAllCartItems(Long userId) {
        if(userId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin để xoá!",null)
            );
        }

        logger.info("[deleteAllCartItems] Xoá toàn bộ giỏ hàng cho userId: {}", userId);

        cartItemRepository.deleteByUserId(userId);
        logger.info("[deleteAllCartItems] ✅ Xoá toàn bộ sản phẩm thành công.");

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Xoá toàn bộ giỏ hàng!", null));
    }
}
