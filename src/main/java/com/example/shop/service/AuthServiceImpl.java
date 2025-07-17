package com.example.shop.service;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.mapper.UserMapper;
import com.example.shop.dto.request.ChangePasswordRequest;
import com.example.shop.dto.request.LoginRequest;
import com.example.shop.dto.request.RefreshTokenRequest;
import com.example.shop.dto.request.UserUpdateRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;
import com.example.shop.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements UserDetailsService, AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserMapper userMapper;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("🔍 [loadUserByUsername] Đang tìm người dùng theo email: {}", email);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.warn(" [loadUserByUsername] Không tìm thấy người dùng với email: {}", email);
            throw new UsernameNotFoundException("Không tìm thấy người dùng!");
        }

        log.debug(" [loadUserByUsername] Tìm thấy người dùng: {}", user);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public ResponseEntity<ApiResponse<UserDTO>> login(LoginRequest loginRequest) {
        log.info(" [login] Bắt đầu xử lý đăng nhập cho: {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            log.warn(" [login] Không tìm thấy người dùng với email: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Email không tồn tại!", null)
            );
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn(" [login] Mật khẩu không đúng cho email: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Sai mật khẩu!", null)
            );
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        log.info(" [login] Access Token: {}", accessToken);
        log.info(" [login] Refresh Token: {}", refreshToken);

        UserDTO userDTO = userMapper.toDto(user);
        userDTO.setToken(accessToken);
        userDTO.setRefreshToken(refreshToken);

        log.info("[login] token: {}",accessToken);

        log.info(" [login] Đăng nhập thành công cho: {}", loginRequest.getEmail());
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Đăng nhập thành công!", userDTO)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(UserUpdateRequest userUpdateRequest) {
        log.info(" [updateUser] Bắt đầu update tài khoản cho id: {}", userUpdateRequest.getId());
        User user = userRepository.findById(userUpdateRequest.getId()).orElse(null);

        if(user == null) {
            log.warn(" [updateUser] Tài khoản không tồn tại trong hệ thống: {}", userUpdateRequest.getId());
            return ResponseEntity.ok(
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), "Tài khoản không tồn tại!", null)
            );
        }

        user.setFullName(userUpdateRequest.getFullName());
        user.setAddress(userUpdateRequest.getAddress());
        user.setPhone(userUpdateRequest.getPhone());

        userRepository.save(user);

        UserDTO userDTO = userMapper.toDto(user);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Update thông tin người dùng thành công!",userDTO));
    }

    public ResponseEntity<ApiResponse<UserDTO>> createUser(User user) {
        log.info(" [createUser] Bắt đầu tạo tài khoản mới cho email: {}", user.getEmail());

        if (userRepository.findByEmail(user.getEmail()) != null) {
            log.warn(" [createUser] Email đã tồn tại trong hệ thống: {}", user.getEmail());
            return ResponseEntity.ok(
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), "Tài khoản đã tồn tại!", null)
            );
        }

        try {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User createdUser = userRepository.save(user);

            UserDTO userDTO = userMapper.toDto(createdUser);

            log.info(" [createUser] Tạo tài khoản thành công với ID: {}", createdUser.getId());

            return ResponseEntity.ok(
                    new ApiResponse<>(HttpStatus.OK.value(), "Tạo người dùng thành công!", userDTO)
            );
        } catch (Exception e) {
            log.error(" [createUser] Lỗi khi tạo tài khoản: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đăng ký không thành công do lỗi hệ thống!", null)
            );
        }
    }

    public ResponseEntity<ApiResponse<RefreshTokenRequest>> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Refresh token không hợp lệ",null));
        }

        String email = jwtTokenProvider.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        RefreshTokenRequest refreshTokenRequest1 = new RefreshTokenRequest();
        refreshTokenRequest1.setRefreshToken(newRefreshToken);
        refreshTokenRequest1.setAccessToken(newAccessToken);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),"Lấy Token mới thành công!",refreshTokenRequest1));
    }

    public ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(changePasswordRequest.getId()).orElseThrow();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(changePasswordRequest.getOldPassowrd(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassowrd()));
            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Đổi mật khẩu thành công!", null));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Sai mật khẩu cũ!", null));
        }
    }

    public ResponseEntity<ApiResponse<String>> uploadAvatar(Long id, String mode, MultipartFile file) {
        String UPLOAD_DIR = mode+"/";

        try {
            // 1. Kiểm tra file có rỗng không
            if (file.isEmpty()) {
                System.out.println("[UPLOAD] File trống!");
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "File không được để trống!", null)
                );
            }

            // 2. Kiểm tra người dùng có tồn tại không
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isEmpty()) {
                System.out.println("[UPLOAD] Không tìm thấy người dùng với id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Không tìm thấy người dùng!", null)
                );
            }

            User user = optionalUser.get();

            // 3. Log thông tin file
            System.out.println("[UPLOAD] Nhận được file: " + file.getOriginalFilename());
            System.out.println("[UPLOAD] Kích thước: " + file.getSize() + " bytes");
            System.out.println("[UPLOAD] Content-Type: " + file.getContentType());

            // 4. Tạo thư mục nếu chưa tồn tại
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                System.out.println("[UPLOAD] Tạo thư mục " + UPLOAD_DIR + ": " + created);
            }

            // 5. Tạo tên file duy nhất và đường dẫn
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);

            // 6. Lưu file vào server
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[UPLOAD] Lưu file tại: " + path.toAbsolutePath());

            // 7. Cập nhật đường dẫn avatar trong database
            user.setAvatarUrl(fileName);  // hoặc lưu cả đường dẫn nếu muốn
            userRepository.save(user);
            System.out.println("[UPLOAD] Đã cập nhật avatar cho user ID: " + id);

            // 8. Trả kết quả thành công
            return ResponseEntity.ok(
                    new ApiResponse<>(HttpStatus.OK.value(), "Tải lên avatar thành công!", fileName)
            );

        } catch (IOException e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Thất bại trong quá trình tải file!", null)
            );
        }
    }


}
