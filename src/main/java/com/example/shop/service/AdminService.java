package com.example.shop.service;

import com.example.shop.model.*;
import com.example.shop.model.user.Role;
import com.example.shop.model.user.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    public ResponseEntity<ApiResponse<List<UserEntity>>> getAllUsers() {
        try {
            List<UserEntity> userEntities = userService.getAllUsers();
            log.info(" [getAllUsers] Lấy danh sách người dùng thành công. Số lượng: "+ userEntities.size());
            return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS,HttpStatus.OK, userEntities);
        } catch (Exception e) {
            log.error(" [getAllUsers] Lỗi khi lấy danh sách người dùng: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
    }

    public ResponseEntity<ApiResponse<String>> setRoleAdmin(Long userId) {
        try {
            UserEntity userEntity = userService.findUserById(userId);

            if (userEntity == null) {
                log.warn(" [setRoleAdmin] Không tìm thấy người dùng với ID: {}", userId);
                return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.NOT_FOUND,null);
            }

            userEntity.setRole(Role.ROLE_ADMIN);
            userService.saveUser(userEntity);

            log.info("[setRoleAdmin] Cập nhật quyền ADMIN thành công cho user ID: {}", userId);
            return ResponseHandler.generateResponse(Messages.USER_UPDATED,HttpStatus.OK,null);
        } catch (Exception e) {
            log.error(" [setRoleAdmin] Lỗi khi cập nhật quyền: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
    }

    public ResponseEntity<ApiResponse<String>> deleteUser(Long userId) {
        try {
            if (userId == null){
                return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
            }
            UserEntity userEntity = userService.findUserById(userId);

            if (userEntity == null) {
                log.warn(" [deleteUser] Không tìm thấy người dùng với ID: {}", userId);
                return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.NOT_FOUND,null);
            }

            userEntity.setDeleted(true);
            userService.saveUser(userEntity);

            log.info(" [deleteUser] Xóa mềm thành công cho user ID: {}", userId);
            return ResponseHandler.generateResponse(Messages.USER_DELETED,HttpStatus.OK,null);
        } catch (Exception e) {
            log.error(" [deleteUser] Lỗi khi xóa người dùng: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
    }
}
