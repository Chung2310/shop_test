package com.example.shop.service;

import com.example.shop.model.Messages;
import com.example.shop.model.ResponseHandler;
import com.example.shop.model.user.UserEntityDTO;
import com.example.shop.mapper.UserMapper;
import com.example.shop.model.auth.ChangePasswordRequest;
import com.example.shop.model.user.UserUpdateRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.user.UserEntity;
import com.example.shop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //admin
    public List<UserEntity> getAllUsers(){
        log.info("[getAllUsers] Lấy toàn bộ thông tin user");
        return userRepository.findAll();
    }

    public UserEntity findUserById(Long userId) {
        log.info("[findUserById] Tìm người dùng với ID: {}", userId);
        return userRepository.findById(userId).orElse(null);
    }

    public UserEntity findUserByUsername(String username) {
        log.info("[findUserByUsername] Tìm người dùng với username: {}", username);
        return userRepository.findUserByFullNameContainingIgnoreCase(username);
    }

    public UserEntity findUserByEmail(String email) {
        log.info("[findUserByEmail] Tìm người dùng với email: {}", email);
        return userRepository.findUserByEmail(email);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        log.info("[saveUser] Lưu người dùng với email: {}", userEntity.getEmail());
        return userRepository.save(userEntity);
    }


    public ResponseEntity<ApiResponse<UserEntityDTO>> updateUser(UserUpdateRequest request) {
        log.info("[updateUser] Bắt đầu cập nhật người dùng ID: {}", request.getId());

        Optional<UserEntity> userOpt = userRepository.findById(request.getId());
        if (!userOpt.isPresent()) {
            log.warn("[updateUser] Người dùng không tồn tại: {}", request.getId());
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.OK, null);
        }

        UserEntity userEntity = userOpt.get();
        userEntity.setFullName(request.getFullName());
        userEntity.setAddress(request.getAddress());
        userEntity.setPhone(request.getPhone());

        userRepository.save(userEntity);
        log.info("[updateUser] Cập nhật thông tin thành công cho user ID: {}", userEntity.getId());

        UserEntityDTO userEntityDTO = userMapper.toDto(userEntity);
        return ResponseHandler.generateResponse(Messages.USER_UPDATED,HttpStatus.OK, userEntityDTO);
    }


    public ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordRequest req) {
        log.info("[changePassword] Thay đổi mật khẩu cho user ID: {}", req.getId());

        UserEntity userEntity = userRepository.findUserById(req.getId());
        if (userEntity == null) {
            log.warn("[changePassword] Không tìm thấy người dùng với ID: {}", req.getId());
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.NOT_FOUND, null);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(req.getOldPassowrd(), userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(req.getNewPassowrd()));
            userRepository.save(userEntity);
            log.info("[changePassword] Đổi mật khẩu thành công cho user ID: {}", userEntity.getId());
            return ResponseHandler.generateResponse(Messages.PASSWORD_CHANGED_SUCCESS,HttpStatus.OK, null);
        } else {
            log.warn("[changePassword] Sai mật khẩu cũ cho user ID: {}", userEntity.getId());
            return ResponseHandler.generateResponse(Messages.OLD_PASSWORD_INCORRECT,HttpStatus.BAD_REQUEST, null);
        }
    }


    public ResponseEntity<ApiResponse<String>> uploadImage(Long id, String mode, MultipartFile file) {
        log.info("[uploadImage] Bắt đầu upload "+mode+" cho user ID: {}", id);
        String UPLOAD_DIR = mode + "/";

        try {
            if (file.isEmpty()) {
                log.warn("[uploadImage] File trống!");
                return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST, null);
            }

            UserEntity userEntity = userRepository.findUserById(id);
            if (userEntity == null) {
                log.warn("[uploadImage] Không tìm thấy người dùng ID: {}", id);
                return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.NOT_FOUND, null);
            }

            log.info("[uploadImage] File nhận được: {}", file.getOriginalFilename());
            log.debug("[uploadImage] Kích thước: {} bytes, Content-Type: {}", file.getSize(), file.getContentType());

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                log.info("[uploadImage] Tạo thư mục '{}': {}", UPLOAD_DIR, created);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            log.info("[uploadImage] Đã lưu file tại: {}", path.toAbsolutePath());

            userEntity.setAvatarUrl(fileName);
            userRepository.save(userEntity);
            log.info("[uploadImage] Cập nhật "+mode+" thành công cho user ID: {}", id);

            return ResponseHandler.generateResponse(Messages.IMAGE_UPLOAD_SUCCESS,HttpStatus.CREATED, fileName);

        } catch (IOException e) {
            log.error("[uploadImage] Lỗi khi upload file: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}

