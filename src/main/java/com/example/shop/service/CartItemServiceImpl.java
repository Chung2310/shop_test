package com.example.shop.service;

import com.example.shop.dto.CartItemDTO;
import com.example.shop.dto.mapper.CartItemMapper;
import com.example.shop.dto.request.CartItemRequest;
import com.example.shop.model.ApiReponse;
import com.example.shop.model.CartItem;
import com.example.shop.repository.BookRepository;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImpl.class);

    @Override
    public ResponseEntity<ApiReponse<List<CartItemDTO>>> getAllCartItems(Long userId) {
        logger.info("[getAllCartItems] Lấy giỏ hàng cho userId: {}", userId);

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        logger.debug("[getAllCartItems] Số lượng sản phẩm trong giỏ hàng: {}", cartItems.size());

        return ResponseEntity.ok(
                new ApiReponse<>(HttpStatus.OK.value(), "Lấy dữ liệu giỏ hàng thành công!", cartItemMapper.toDTOList(cartItems))
        );
    }

    @Override
    public ResponseEntity<ApiReponse<CartItemDTO>> addItemToCart(CartItemRequest cartItemRequest) {
        logger.info("[addItemToCart] Thêm sản phẩm vào giỏ: userId={}, bookId={}, quantity={}",
                cartItemRequest.getUserId(), cartItemRequest.getBookId(), cartItemRequest.getQuantity());

        Optional<CartItem> cartItemOptional = cartItemRepository.findByUserIdAndBookId(
                cartItemRequest.getUserId(), cartItemRequest.getBookId());

        CartItem cartItem = cartItemOptional.orElseGet(() -> {
            logger.debug("[addItemToCart] Sản phẩm chưa tồn tại trong giỏ. Tiến hành tạo mới.");

            CartItem newCartItem = new CartItem();
            newCartItem.setUser(
                    userRepository.findById(cartItemRequest.getUserId())
                            .orElseThrow(() -> {
                                logger.error("[addItemToCart] ❌ Không tìm thấy User với ID: {}", cartItemRequest.getUserId());
                                return new RuntimeException("Không tìm thấy người dùng!");
                            })
            );
            newCartItem.setBook(
                    bookRepository.findById(cartItemRequest.getBookId())
                            .orElseThrow(() -> {
                                logger.error("[addItemToCart] ❌ Không tìm thấy Book với ID: {}", cartItemRequest.getBookId());
                                return new RuntimeException("Không tìm thấy sách!");
                            })
            );
            newCartItem.setQuantity(cartItemRequest.getQuantity());
            return newCartItem;
        });

        if (cartItemOptional.isPresent()) {
            logger.debug("[addItemToCart] Sản phẩm đã có sẵn trong giỏ. Không tạo mới.");
        }

        cartItemRepository.save(cartItem);
        logger.info("[addItemToCart] ✅ Đã lưu CartItem thành công.");
        return ResponseEntity.ok(new ApiReponse<>(HttpStatus.OK.value(), "Thêm vào giỏ hàng thành công!", cartItemMapper.toDTO(cartItem)));
    }

    @Override
    public ResponseEntity<ApiReponse<CartItemDTO>> updateItemToCart(CartItemRequest cartItemRequest) {
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
        return ResponseEntity.ok(new ApiReponse<>(HttpStatus.OK.value(), "Cập nhật số lượng thành công!", cartItemMapper.toDTO(item)));
    }

    @Override
    public ResponseEntity<ApiReponse<String>> deleteItem(Long userId, Long bookId) {
        logger.info("[deleteItem] Xoá sản phẩm: userId={}, bookId={}", userId, bookId);

        CartItem item = cartItemRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> {
                    logger.error("[deleteItem] ❌ Không tìm thấy sản phẩm để xoá");
                    return new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
                });

        cartItemRepository.delete(item);
        logger.info("[deleteItem] ✅ Xoá sản phẩm thành công.");
        return ResponseEntity.ok(new ApiReponse<>(HttpStatus.OK.value(), "Xoá item khỏi giỏ hàng thành công!", null));
    }

    @Override
    public ResponseEntity<ApiReponse<String>> deleteAllCartItems(Long userId) {
        logger.info("[deleteAllCartItems] Xoá toàn bộ giỏ hàng cho userId: {}", userId);

        cartItemRepository.deleteByUserId(userId);
        logger.info("[deleteAllCartItems] ✅ Xoá toàn bộ sản phẩm thành công.");

        return ResponseEntity.ok(new ApiReponse<>(HttpStatus.OK.value(), "Xoá toàn bộ giỏ hàng!", null));
    }
}
