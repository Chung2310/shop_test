package com.example.shop.model;

public class Messages {
    private Messages() {} // chặn khởi tạo

    // Thông báo chung cho sản phẩm
    public static final String PRODUCT_NOT_FOUND = "Không tìm thấy sản phẩm";
    public static final String PRODUCT_CREATED = "Tạo sản phẩm thành công";
    public static final String PRODUCT_UPDATED = "Cập nhật sản phẩm thành công";
    public static final String PRODUCT_DELETED = "Xóa sản phẩm thành công";

    // Thông báo chung cho đơn hàng
    public static final String ORDER_NOT_FOUND = "Đơn hàng không tồn tại!";
    public static final String ORDER_UPDATED = "Cập nhập đơn hàng thành công!";
    public static final String ORDER_CONFIRMED = "Xác nhận đơn hàng thành công!";
    public static final String ORDER_CREATED = "Tạo đơn hàng thành công!";
    public static final String ORDER_FETCH_SUCCESS = "Lấy danh sách đơn hàng thành công!";
    public static final String ORDER_ALREADY_CANCELLED = "Đơn hàng đã bị huỷ trước đó!";
    public static final String ORDER_CANCEL_SUCCESS = "Huỷ đơn hàng thành công!";

    // Thông báo chung cho user
    public static final String USER_NOT_FOUND = "Người dùng không tồn tại";
    public static final String USER_CREATED = "Tạo người dùng thành công";
    public static final String USER_UPDATED = "Cập nhập người dùng thanh công";
    public static final String USER_DELETED = "Xoá người dùng thành công";
    public static final String USER_ALREADY_EXISTS = "Người dùng đã tồn tại";
    public static final String PASSWORD_CHANGED_SUCCESS = "Đổi mật khẩu thành công!";


    // Thông báo lỗi liên quan đến kho hàng / giỏ hàng
    public static final String CART_FETCH_SUCCESS = "Lấy dữ liệu giỏ hàng thành công!";
    public static final String PRODUCT_QUANTITY_NOT_ENOUGH = "Số lượng sản phẩm còn lại không đủ!";
    public static final String CART_ADD_SUCCESS = "Thêm vào giỏ hàng thành công!";
    public static final String CART_UPDATE_QUANTITY_SUCCESS = "Cập nhật số lượng thành công!";
    public static final String CART_REMOVE_ITEM_SUCCESS = "Xoá item khỏi giỏ hàng thành công!";
    public static final String CART_CLEAR_SUCCESS = "Xoá toàn bộ giỏ hàng thành công!";

    //Thông báo chung cho chat, message, contact
    public static final String CHAT_MESSAGE_SENT_SUCCESS = "Lưu tin nhắn thành công!";
    public static final String CHAT_HISTORY_FETCH_SUCCESS = "Lấy danh sách lịch sử thành công!";
    public static final String CONTACT_CREATED_SUCCESS = "Tạo liên hệ thành công!";
    public static final String CONTACT_DELETED_SUCCESS = "Xoá liên hệ thành công!";

    //Thông báo chung cho đánh giá, like đánh giá
    public static final String REVIEW_NOT_FOUND = "Không tìm thấy đánh giá!";
    public static final String PRODUCT_ALREADY_REVIEWED = "Người dùng đã đánh giá sản phẩm!";
    public static final String REVIEW_CREATED = "Tạo đánh giá thành công!";
    public static final String REVIEW_DELETED = "Xoá đánh giá thành công!";
    public static final String REVIEW_UPDATED = "Sửa thông tin đánh giá thành công!";
    public static final String REVIEW_FETCH_SUCCESS = "Lấy danh sách đánh giá của sản phẩm thành công!";
    public static final String REVIEW_CHECKED = "Kiểm tra thành công!";

    //Thông báo chung cho sản phẩm yêu thích
    public static final String WISH_LIST_NOT_FOUND = "Không tìm thấy sản phẩm yêu thích";
    public static final String WISHLIST_FETCH_SUCCESS = "Lấy danh sách yêu thích thành công!";
    public static final String WISHLIST_ADD_SUCCESS = "Thêm vào danh sách yêu thích thành công!";
    public static final String WISHLIST_REMOVE_SUCCESS = "Xoá khỏi danh sách yêu thích thành công!";


    // Thông báo chung khác
    public static final String SYSTEM_ERROR = "Lỗi hệ thống, vui lòng thử lại sau";
    public static final String INVALID_INPUT = "Thông tin đầu vào không hợp lệ";

    // Thông báo cho đăng nhập & xác thưc
    public static final String PASSWORD_RESET_ALREADY_REQUESTED =
            "Bạn đã yêu cầu reset mật khẩu trước đó. Vui lòng kiểm tra email!";
    public static final String PASSWORD_RESET_REQUEST_SUCCESS =
            "Yêu cầu quên mật khẩu đã được gửi thành công!";
    public static final String TOKEN_EXPIRED =
            "Token đã hết hạn!";
    public static final String PASSWORD_RESET_SUCCESS = "Reset mật khẩu thành công!";
    public static final String REFRESH_TOKEN_SUCCESS = "Refresh token thành công";
    public static final String INVALID_CREDENTIALS = "Sai tài khoản hoặc mật khẩu";
    public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
    public static final String OLD_PASSWORD_INCORRECT = "Sai mật khẩu cũ!";

    // Thông báo chung phổ biến
    public static final String DATA_FETCH_SUCCESS = "Lấy dữ liệu thành công!";
    public static final String MISSING_REQUIRED_INFO = "Thiếu thông tin đầu vào!";
    public static final String CONNECT_SERVER_SUCCESS = "Kết nốt đến server thành công!";
    public static final String IMAGE_UPLOAD_SUCCESS = "Tải hình ảnh lên thành công!";


}
