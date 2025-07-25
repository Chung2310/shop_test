package com.example.shop.service;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.mapper.UserMapper;
import com.example.shop.dto.request.ChangePasswordRequest;
import com.example.shop.dto.request.UserUpdateRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //admin
    public List<User> getAllUsers(){
        log.info("[getAllUsers] Lấy toàn bộ thông tin user");
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        log.info("[findUserById] Tìm người dùng với ID: {}", userId);
        return userRepository.findById(userId).orElse(null);
    }

    public User findUserByUsername(String username) {
        log.info("[findUserByUsername] Tìm người dùng với username: {}", username);
        return userRepository.findUserByFullNameContainingIgnoreCase(username);
    }

    public User findUserByEmail(String email) {
        log.info("[findUserByEmail] Tìm người dùng với email: {}", email);
        return userRepository.findUserByEmail(email);
    }

    public User saveUser(User user) {
        log.info("[saveUser] Lưu người dùng với email: {}", user.getEmail());
        return userRepository.save(user);
    }


    @Override
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(UserUpdateRequest request) {
        log.info("[updateUser] Bắt đầu cập nhật người dùng ID: {}", request.getId());

        Optional<User> userOpt = userRepository.findById(request.getId());
        if (!userOpt.isPresent()) {
            log.warn("[updateUser] Người dùng không tồn tại: {}", request.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Tài khoản không tồn tại!", null)
            );
        }

        User user = userOpt.get();
        user.setFullName(request.getFullName());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());

        userRepository.save(user);
        log.info("[updateUser] Cập nhật thông tin thành công cho user ID: {}", user.getId());

        UserDTO userDTO = userMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Update thông tin người dùng thành công!", userDTO)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordRequest req) {
        log.info("[changePassword] Thay đổi mật khẩu cho user ID: {}", req.getId());

        User user = userRepository.findUserById(req.getId());
        if (user == null) {
            log.warn("[changePassword] Không tìm thấy người dùng với ID: {}", req.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Người dùng không tồn tại!", null)
            );
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(req.getOldPassowrd(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(req.getNewPassowrd()));
            userRepository.save(user);
            log.info("[changePassword] Đổi mật khẩu thành công cho user ID: {}", user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(HttpStatus.OK.value(), "Đổi mật khẩu thành công!", null)
            );
        } else {
            log.warn("[changePassword] Sai mật khẩu cũ cho user ID: {}", user.getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Sai mật khẩu cũ!", null)
            );
        }
    }


    public ResponseEntity<ApiResponse<String>> uploadImage(Long id, String mode, MultipartFile file) {
        log.info("[uploadImage] Bắt đầu upload "+mode+" cho user ID: {}", id);
        String UPLOAD_DIR = mode + "/";

        try {
            if (file.isEmpty()) {
                log.warn("[uploadImage] File trống!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "File không được để trống!", null)
                );
            }

            User user = userRepository.findUserById(id);
            if (user == null) {
                log.warn("[uploadImage] Không tìm thấy người dùng ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Không tìm thấy người dùng!", null)
                );
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

            user.setAvatarUrl(fileName);
            userRepository.save(user);
            log.info("[uploadImage] Cập nhật "+mode+" thành công cho user ID: {}", id);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(HttpStatus.OK.value(), "Tải lên "+mode+" thành công!", fileName)
            );

        } catch (IOException e) {
            log.error("[uploadImage] Lỗi khi upload file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Thất bại trong quá trình tải file!", null)
            );
        }
    }
}

