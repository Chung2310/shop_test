package com.example.shop.service;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.mapper.UserMapper;
import com.example.shop.dto.request.LoginRequest;
import com.example.shop.dto.request.RefreshTokenRequest;
import com.example.shop.model.ApiResponse;
import com.example.shop.model.User;
import com.example.shop.security.JwtTokenProvider;
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

import java.util.*;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordResetService passwordResetService;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("🔍 [loadUserByUsername] Đang tìm người dùng theo email: {}", email);
        if(email == null ){
            return null;
        }

        User user = userService.findUserByEmail(email);

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
        if(loginRequest.getEmail() == null || loginRequest.getPassword() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin đăng nhập!",null)
            );
        }

        User user = userService.findUserByEmail(loginRequest.getEmail());
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

    public ResponseEntity<ApiResponse<UserDTO>> register(User user) {
        log.info("Yêu cầu đăng ký với email: {}", user.getEmail());
        if(user.getEmail() == null || user.getPassword() == null || user.getFullName() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin người dùng!",null)
            );
        }

        // Kiểm tra nếu email đã tồn tại
        if (userService.findUserByEmail(user.getEmail()) != null) {
            log.warn("Email {} đã tồn tại trong hệ thống.", user.getEmail());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, "Tài khoản đã tồn tại!", null));
        }

        // Mã hóa mật khẩu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.debug("Mật khẩu đã được mã hóa cho email: {}", user.getEmail());

        // Lưu người dùng
        User savedUser = userService.saveUser(user);
        log.info("Tạo mới người dùng thành công: ID = {}, Email = {}", savedUser.getId(), savedUser.getEmail());

        UserDTO userDTO = userMapper.toDto(savedUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Tạo người dùng mới thành công!", userDTO));
    }

    public ResponseEntity<ApiResponse<RefreshTokenRequest>> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            if(refreshTokenRequest.getAccessToken() == null || refreshTokenRequest.getRefreshToken() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                                "Thiếu thông tin yêu cầu!",null)
                );
            }

            if (!jwtTokenProvider.validateToken(refreshTokenRequest.getRefreshToken())) {
                log.warn("Refresh token không hợp lệ hoặc đã hết hạn");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Refresh token không hợp lệ",null));
            }

            // 2. Trích xuất username từ token cũ
            String email = jwtTokenProvider.extractUsername(refreshTokenRequest.getRefreshToken());
            log.info("Đang làm mới token cho user: {}", email);

            // 3. Tìm user và tạo token mới
            User user = userService.findUserByEmail(email);

            RefreshTokenRequest request = new RefreshTokenRequest();
            request.setAccessToken(jwtTokenProvider.generateAccessToken(user));
            request.setRefreshToken(jwtTokenProvider.generateRefreshToken(user));

            log.info("Token mới được tạo cho user: {}", email);

            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),"Refresh token thành công!" ,request));

        } catch (Exception e) {
            log.error("Lỗi khi làm mới token: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống khi làm mới token",null));
        }
    }

    public ResponseEntity<ApiResponse<String>> forgotPassword(String email) {
        log.info("📩 Yêu cầu quên mật khẩu cho email: {}", email);

        if (email == null) {
            log.warn("❌ Thiếu thông tin email trong yêu cầu forgotPassword");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu", null)
            );
        }

        ResponseEntity<ApiResponse<String>> response = passwordResetService.createPasswordResetTokenForEmail(email);
        log.info("📤 Kết quả forgotPassword cho {}: {}", email, response.getBody().getMessage());
        return response;
    }

    public ResponseEntity<ApiResponse<String>> resetPassword(Map<String, String> body) {
        log.info("🔑 Yêu cầu reset mật khẩu với dữ liệu: {}", body);

        String token = body.get("token");
        String newPassword = body.get("newPassword");

        if (token == null || newPassword == null) {
            log.warn("❌ Thiếu token hoặc mật khẩu mới trong resetPassword");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin yêu cầu", null)
            );
        }

        log.debug("📄 Token nhận được: {}", token);
        log.debug("🔒 Mật khẩu mới (ẩn log giá trị thực tế để bảo mật)");

        passwordResetService.resetPassword(token, newPassword);
        log.info("✅ Reset password thành công cho token: {}", token);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Reset password thành công!", null)
        );
    }
}
