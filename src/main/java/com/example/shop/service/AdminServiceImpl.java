package com.example.shop.service;

import com.example.shop.model.ApiReponse;
import com.example.shop.model.User;
import com.example.shop.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public ResponseEntity<ApiReponse<List<User>>> getAllUsers() {
        try {
            List<User> users = adminRepository.findAll();
            log.info(" [getAllUsers] Lấy danh sách người dùng thành công. Số lượng: {}", users.size());
            return ResponseEntity.ok(
                    new ApiReponse<>(HttpStatus.OK.value(), "Lấy danh sách người dùng thành công!", users)
            );
        } catch (Exception e) {
            log.error(" [getAllUsers] Lỗi khi lấy danh sách người dùng: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiReponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống khi lấy danh sách người dùng!", null));
        }
    }

    @Override
    public ResponseEntity<ApiReponse<User>> setRoleAdmin(Long userId) {
        try {
            Optional<User> userOptional = adminRepository.findById(userId);

            if (userOptional.isEmpty()) {
                log.warn(" [setRoleAdmin] Không tìm thấy người dùng với ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiReponse<>(HttpStatus.NOT_FOUND.value(), "Người dùng không tồn tại!", null));
            }

            User user = userOptional.get();
            user.setRole("ADMIN");
            adminRepository.save(user);

            log.info("[setRoleAdmin] Cập nhật quyền ADMIN thành công cho user ID: {}", userId);
            return ResponseEntity.ok(
                    new ApiReponse<>(HttpStatus.OK.value(), "Cập nhật quyền ADMIN thành công!", user)
            );

        } catch (Exception e) {
            log.error(" [setRoleAdmin] Lỗi khi cập nhật quyền: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiReponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi cập nhật quyền người dùng!", null));
        }
    }

    @Override
    public ResponseEntity<ApiReponse<String>> deleteUser(Long userId) {
        try {
            Optional<User> userOptional = adminRepository.findById(userId);

            if (userOptional.isEmpty()) {
                log.warn(" [deleteUser] Không tìm thấy người dùng với ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiReponse<>(HttpStatus.NOT_FOUND.value(), "Người dùng không tồn tại!", null));
            }

            User user = userOptional.get();
            user.setIsDeleted(true);
            adminRepository.save(user);

            log.info(" [deleteUser] Xóa mềm thành công cho user ID: {}", userId);
            return ResponseEntity.ok(
                    new ApiReponse<>(HttpStatus.NO_CONTENT.value(), "Xóa người dùng thành công!", null)
            );
        } catch (Exception e) {
            log.error(" [deleteUser] Lỗi khi xóa người dùng: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiReponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống khi xóa người dùng!", null));
        }
    }
}
