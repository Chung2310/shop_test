package com.example.shop.service;

import com.example.shop.model.order.*;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.model.*;
import com.example.shop.model.product.Product;
import com.example.shop.model.user.UserEntity;
import com.example.shop.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService bookService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private EmailService emailService;

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    public ResponseEntity<ApiResponse<String>> createOrder(OrderRequest orderRequest) {
        log.info("[createOrder] Bắt đầu tạo đơn hàng...");
        if (orderRequest == null) {
            log.warn("[createOrder] Dữ liệu đơn hàng null!");
            return ResponseHandler.generateResponse(Messages.INVALID_INPUT,HttpStatus.BAD_REQUEST, null);
        }

        if (orderRequest.getUserId() == null ) {
            log.warn("[createOrder] Người dùng không hợp lệ: user.id == null");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        UserEntity userEntity = userService.findUserById(orderRequest.getUserId());

        Order order = new Order();
        order.setUserEntity(userEntity);
        order.setDescription(orderRequest.getDescription());
        order.setAddress(orderRequest.getAddress());
        order.setPhone(orderRequest.getPhone());

        Double total = 0.0;
        List<OrderDetail> detailList = new ArrayList<>();

        for (OrderRequest.OrderItem item : orderRequest.getItems()) {
            Product product = bookService.findBookById(item.getBookId());

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(product.getPrice());

            product.setQuantityPurchased(product.getQuantityPurchased()+ item.getQuantity());
            product.setQuantity(product.getQuantity()-item.getQuantity());
            bookService.createOrUpdateProductDTO(product);

            total += product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())).doubleValue();

            detailList.add(orderDetail);
        }

        cartItemService.deleteAllCartItems(userEntity.getId());

        order.setPrice(BigDecimal.valueOf(total));
        order.setOrderDetails(detailList);

        log.info("[createOrder] Tạo đơn hàng cho userId = {}", orderRequest.getUserId());
        Order savedOrder = saveOrUpdateOrder(order);
        log.info("[createOrder] Đã lưu đơn hàng thành công. OrderId = {}", savedOrder.getId());

        String toEmail = userEntity.getEmail();
        String subject = "Xác nhận đơn hàng #" + savedOrder.getId();
        String body = "Xin chào " + userEntity.getFullName() + ",\n\n" +
                "Cảm ơn bạn đã đặt hàng tại cửa hàng của chúng tôi.\n" +
                "Mã đơn hàng: " + savedOrder.getId() + "\n" +
                "Trạng thái: " + savedOrder.getOrderStatus() + "\n" +
                "Tổng tiền: " + savedOrder.getPrice() + " VND\n\n" +
                "Trân trọng,\nCửa hàng";

        emailService.sendEmail(toEmail, subject, body);

        return ResponseHandler.generateResponse(Messages.ORDER_CREATED,HttpStatus.CREATED, null);
    }

    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrderByUserId(Long userId) {
        log.info("[getOrderByUserId] Bắt đầu lấy đơn hàng theo userId = {}", userId);
        try {
            UserEntity userEntity = userService.findUserById(userId);
            if (userEntity == null) {
                log.warn("[getOrderByUserId] Không tìm thấy userId = {}", userId);
                return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.NOT_FOUND, null);
            }

            log.info("[getOrderByUserId] Tìm thấy userId = {}. Tiến hành truy vấn đơn hàng.", userId);
            List<Order> ordersList = orderRepository.findOrderByUserEntityId(userId);
            log.info("[getOrderByUserId] Truy vấn thành công. Số đơn hàng: {}", ordersList.size());

            List<OrderResponse> orderResponseList = orderMapper.toOrderResponseList(ordersList);

            return ResponseHandler.generateResponse(Messages.ORDER_FETCH_SUCCESS,HttpStatus.OK, orderResponseList);

        } catch (RuntimeException e) {
            log.error("[getOrderByUserId] Lỗi hệ thống khi truy vấn đơn hàng: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        log.info("[getAllOrders] Bắt đầu lấy tất cả đơn hàng...");
        try {
            List<Order> ordersList = orderRepository.findAll();
            log.info("[getAllOrders] Thành công. Tổng số đơn hàng: {}", ordersList.size());

            List<OrderResponse> orderResponseList = orderMapper.toOrderResponseList(ordersList);

            return ResponseHandler.generateResponse(Messages.ORDER_FETCH_SUCCESS,HttpStatus.OK, orderResponseList);
        } catch (RuntimeException e) {
            log.error("[getAllOrders] Lỗi hệ thống khi lấy danh sách đơn hàng: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public Order saveOrUpdateOrder(Order order) {
        if (order == null) {
            log.warn("[updateOrder] Đơn hàng null, không thể cập nhật!");
            return null;
        }

        log.info("[updateOrder] Cập nhật đơn hàng có ID = {}", order.getId());
        order = orderRepository.save(order);
        log.info("[updateOrder] Đã lưu đơn hàng thành công!");
        return order;
    }

    public ResponseEntity<ApiResponse<String>> updateOrderContactInfo(OrderInfoRequest orderInfoRequest) {
        log.info("Yêu cầu cập nhật thông tin đơn hàng: orderId={}, địa chỉ={}, số điện thoại={}, ghi chú={}",
                orderInfoRequest.getOrderId(), orderInfoRequest.getAddress(),
                orderInfoRequest.getPhone(), orderInfoRequest.getDescription());

        Order order = orderRepository.findOrderById(orderInfoRequest.getOrderId());

        UserEntity userEntity = order.getUserEntity();

        if (order == null) {
            log.warn("Không tìm thấy đơn hàng với ID: {}", orderInfoRequest.getOrderId());
            return ResponseHandler.generateResponse(Messages.ORDER_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        log.info("Tìm thấy đơn hàng ID: {}. Tiến hành cập nhật...", order.getId());

        if (orderInfoRequest.getAddress() != null) {
            log.debug("Cập nhật địa chỉ: {} -> {}", order.getAddress(), orderInfoRequest.getAddress());
            order.setAddress(orderInfoRequest.getAddress());
        }

        if (orderInfoRequest.getPhone() != null) {
            log.debug("Cập nhật số điện thoại: {} -> {}", order.getPhone(), orderInfoRequest.getPhone());
            order.setPhone(orderInfoRequest.getPhone());
        }

        if (orderInfoRequest.getDescription() != null) {
            log.debug("Cập nhật ghi chú: {} -> {}", order.getDescription(), orderInfoRequest.getDescription());
            order.setDescription(orderInfoRequest.getDescription());
        }
        order.setUpdatedAt(LocalDateTime.now());
        saveOrUpdateOrder(order);

        String toEmail = userEntity.getEmail();
        String subject = "Cập nhập đơn hàng #" + order.getId();
        String body = "Xin chào " + userEntity.getFullName() + ",\n\n" +
                "Bạn đã đổi thông tin đơn hàng thành công!,\n" +
                "Mã đơn hàng: " + order.getId() + "\n" +
                "Địa chị nhận hàng: " + order.getAddress() + "\n" +
                "Số điện thoại: " + order.getPhone() + "\n" +
                "Chú thích: " + order.getDescription() + "\n" +
                "Trân trọng,\nCửa hàng";

        emailService.sendEmail(toEmail, subject, body);

        log.info("Cập nhật đơn hàng ID {} thành công.", order.getId());

        return ResponseHandler.generateResponse(Messages.ORDER_UPDATED,HttpStatus.NO_CONTENT, null);
    }

    //Admin
    public ResponseEntity<ApiResponse<String>> updateOrderStatus(Long orderId, String orderStatus) {
        log.info("Yêu cầu cập nhật trạng thái đơn hàng: ID = {}, trạng thái mới = {}", orderId, orderStatus);

        Order order = orderRepository.findOrderById(orderId);

        if (order == null) {
            log.warn("Không tìm thấy đơn hàng với ID: {}", orderId);
            return ResponseHandler.generateResponse(Messages.ORDER_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        log.debug("Trạng thái đơn hàng cũ: {}", order.getOrderStatus());

        order.setOrderStatus(orderStatus);
        saveOrUpdateOrder(order);

        log.info("Cập nhật trạng thái đơn hàng ID {} thành công: trạng thái mới = {}", orderId, orderStatus);
        return ResponseHandler.generateResponse(Messages.ORDER_UPDATED,HttpStatus.OK, null);
    }

    public ResponseEntity<ApiResponse<String>> cancelOrder(Long orderId) {
        log.info("Yêu cầu huỷ đơn hàng với ID: {}", orderId);

        Order order = orderRepository.findOrderById(orderId);

        UserEntity userEntity = order.getUserEntity();

        if (order == null) {
            log.warn("Không tìm thấy đơn hàng với ID: {}", orderId);
            return ResponseHandler.generateResponse(Messages.ORDER_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        if (order.isCanceled()) {
            log.warn("Đơn hàng ID {} đã bị huỷ trước đó.", orderId);
            return ResponseHandler.generateResponse(Messages.ORDER_ALREADY_CANCELLED,HttpStatus.CONFLICT, null);
        }

        order.setCanceled(true);
        order.setUpdatedAt(LocalDateTime.now());

        saveOrUpdateOrder(order);

        String toEmail = userEntity.getEmail();
        String subject = "Huỷ đơn hàng #" + order.getId();
        String body = "Xin chào " + userEntity.getFullName() + ",\n\n" +
                "Bạn đã xác nhận huỷ đơn hàng.\n" +
                "Mã đơn hàng: " + order.getId() + "\n" +
                "Thời gian huỷ: " + order.getUpdatedAt() + "\n" +
                "Trân trọng,\nCửa hàng";

        emailService.sendEmail(toEmail, subject, body);
        log.info("Đơn hàng ID {} đã được huỷ thành công.", orderId);

        return ResponseHandler.generateResponse(Messages.ORDER_CANCEL_SUCCESS,HttpStatus.NO_CONTENT, null);

    }

    public ResponseEntity<ApiResponse<List<Order>>> getOrders() {
        log.info(" [Admin] Yêu cầu lấy danh sách đơn hàng");

        List<Order> orders = orderRepository.findAll();
        log.debug(" Số đơn hàng lấy được: {}", orders != null ? orders.size() : 0);

        if (orders == null) {
            log.error(" Không thể truy xuất dữ liệu đơn hàng (orders=null)");
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

        log.info(" Lấy danh sách đơn hàng thành công");
        return ResponseHandler.generateResponse(Messages.ORDER_FETCH_SUCCESS,HttpStatus.OK, orders);
    }

    public ResponseEntity<ApiResponse<String>> confirmOrder(Long orderId) {
        log.info(" [Admin] Yêu cầu xác nhận đơn hàng với ID = {}", orderId);

        if (orderId == null) {
            log.warn("️ Thiếu thông tin orderId");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
        }

        Order order = orderRepository.findOrderById(orderId);
        if (order == null) {
            log.warn("️ Đơn hàng ID = {} không tồn tại", orderId);
            return ResponseHandler.generateResponse(Messages.ORDER_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        log.info(" Cập nhật trạng thái đơn hàng ID = {} → received = true", orderId);
        order.setReceived(true);
        saveOrUpdateOrder(order);

        log.info(" Xác nhận đơn hàng ID = {} thành công", orderId);
        return ResponseHandler.generateResponse(Messages.ORDER_CONFIRMED,HttpStatus.CONFLICT, null);
    }
}
