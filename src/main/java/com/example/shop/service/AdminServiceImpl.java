package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserServiceImpl userService;

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Override
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            log.info(" [getAllUsers] Lấy danh sách người dùng thành công. Số lượng: "+users.size());
            return ResponseEntity.status(HttpStatus.OK.value()).body(new ApiResponse<>(HttpStatus.OK.value(),
                    "Lấy danh sách người dùng thành công!", users));
        } catch (Exception e) {
            log.error(" [getAllUsers] Lỗi khi lấy danh sách người dùng: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lỗi hệ thống khi lấy danh sách người dùng!", null));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<User>> setRoleAdmin(Long userId) {
        try {
            User user = userService.findUserById(userId);

            if (user == null) {
                log.warn(" [setRoleAdmin] Không tìm thấy người dùng với ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                                "Người dùng không tồn tại!", null));
            }

            user.setRole("ADMIN");
            userService.saveUser(user);

            log.info("[setRoleAdmin] Cập nhật quyền ADMIN thành công cho user ID: {}", userId);
            return ResponseEntity.status(HttpStatus.OK.value()).body(
                    new ApiResponse<>(HttpStatus.OK.value(),
                            "Cập nhật quyền ADMIN thành công!", user));

        } catch (Exception e) {
            log.error(" [setRoleAdmin] Lỗi khi cập nhật quyền: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lỗi khi cập nhật quyền người dùng!", null));
        }
    }

    @Override
    public ResponseEntity<ApiResponse<String>> deleteUser(Long userId) {
        try {
            User user = userService.findUserById(userId);

            if (user == null) {
                log.warn(" [deleteUser] Không tìm thấy người dùng với ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                                "Người dùng không tồn tại!", null));
            }

            user.setDeleted(true);
            userService.saveUser(user);

            log.info(" [deleteUser] Xóa mềm thành công cho user ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
                    new ApiResponse<>(HttpStatus.NO_CONTENT.value(),
                            "Xóa người dùng thành công!", null));
        } catch (Exception e) {
            log.error(" [deleteUser] Lỗi khi xóa người dùng: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lỗi hệ thống khi xóa người dùng!", null));
        }
    }
}
