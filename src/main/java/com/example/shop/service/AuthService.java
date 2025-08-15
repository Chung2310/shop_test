package com.example.shop.service;

import com.example.shop.model.auth.PasswordResetToken;
import com.example.shop.model.auth.register.RegisterRequest;
import com.example.shop.model.user.Role;
import com.example.shop.mapper.UserMapper;
import com.example.shop.model.auth.login.LoginRequest;
import com.example.shop.model.auth.RefreshTokenRequest;
import com.example.shop.model.*;
import com.example.shop.model.user.UserEntity;
import com.example.shop.model.user.UserResponse;
import com.example.shop.repository.PasswordResetTokenRepository;
import com.example.shop.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.frontend.reset-url}")
    private String resetUrlPrefix;


    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("🔍 [loadUserByUsername] Đang tìm người dùng theo email: {}", email);
        if(email == null ){
            return null;
        }

        UserEntity userEntity = userService.findUserByEmail(email);

        if (userEntity == null) {
            log.warn(" [loadUserByUsername] Không tìm thấy người dùng với email: {}", email);
            throw new UsernameNotFoundException("Không tìm thấy người dùng!");
        }

        log.debug(" [loadUserByUsername] Tìm thấy người dùng: {}", userEntity);

        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                new ArrayList<>()
        );
    }

    public ResponseEntity<ApiResponse<UserResponse>> login(LoginRequest loginRequest) {
        log.info(" [login] Bắt đầu xử lý đăng nhập cho: {}", loginRequest.getEmail());
        if(loginRequest.getEmail() == null || loginRequest.getPassword() == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        UserEntity userEntity = userService.findUserByEmail(loginRequest.getEmail());
        if (userEntity == null) {
            log.warn(" [login] Không tìm thấy người dùng với email: {}", loginRequest.getEmail());
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.NOT_FOUND,null);
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            log.warn(" [login] Mật khẩu không đúng cho email: {}", loginRequest.getEmail());
            return ResponseHandler.generateResponse(Messages.INVALID_CREDENTIALS,HttpStatus.UNAUTHORIZED,null);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(userEntity);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userEntity);

        log.info(" [login] Access Token: {}", accessToken);
        log.info(" [login] Refresh Token: {}", refreshToken);

        UserResponse userResponse = userMapper.toUserResponse(userEntity);
        userResponse.setToken(accessToken);
        userResponse.setRefreshToken(refreshToken);

        log.info("[login] token: {}",accessToken);

        log.info(" [login] Đăng nhập thành công cho: {}", loginRequest.getEmail());

        return ResponseHandler.generateResponse(Messages.LOGIN_SUCCESS,HttpStatus.OK, userResponse);
    }

    public ResponseEntity<ApiResponse<String>> register(RegisterRequest registerRequest) {

        String email = registerRequest.getEmail();
        String fullName = registerRequest.getFullName();

        log.info("Yêu cầu đăng ký với email: {}", email);
        if(email == null || registerRequest.getPassword() == null || fullName == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        // Kiểm tra nếu email đã tồn tại
        if (userService.findUserByEmail(email) != null) {
            log.warn("Email {} đã tồn tại trong hệ thống.", email);
            return ResponseHandler.generateResponse(Messages.USER_ALREADY_EXISTS,HttpStatus.CONFLICT,null);
        }

        // Mã hóa mật khẩu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserEntity user = new UserEntity();
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(Role.valueOf("ROLE_"+registerRequest.getRole()));

        log.debug("Mật khẩu đã được mã hóa cho email: {}", email);
        if (user.getRole() == Role.ROLE_SELLER){
            user.setRole(Role.ROLE_SELLER);
        } else{
            user.setRole(Role.ROLE_CUSTOMER);
        }

        UserEntity savedUserEntity = userService.saveUser(user);
        log.info("Tạo mới người dùng thành công: ID = {}, Email = {}", savedUserEntity.getId(), savedUserEntity.getEmail());

        return ResponseHandler.generateResponse(Messages.USER_CREATED,HttpStatus.CREATED, null);
    }

    public ResponseEntity<ApiResponse<RefreshTokenRequest>> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            if(refreshTokenRequest.getAccessToken() == null || refreshTokenRequest.getRefreshToken() == null){
                return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
            }

            if (!jwtTokenProvider.validateToken(refreshTokenRequest.getRefreshToken())) {
                log.warn("Refresh token không hợp lệ hoặc đã hết hạn");
                return ResponseHandler.generateResponse(Messages.INVALID_INPUT,HttpStatus.UNAUTHORIZED,null);
            }

            // 2. Trích xuất username từ token cũ
            String email = jwtTokenProvider.extractUsername(refreshTokenRequest.getRefreshToken());
            log.info("Đang làm mới token cho user: {}", email);

            // 3. Tìm user và tạo token mới
            UserEntity userEntity = userService.findUserByEmail(email);

            RefreshTokenRequest response = new RefreshTokenRequest();
            response.setAccessToken(jwtTokenProvider.generateAccessToken(userEntity));
            response.setRefreshToken(jwtTokenProvider.generateRefreshToken(userEntity));

            log.info("Token mới được tạo cho user: {}", email);

            return ResponseHandler.generateResponse(Messages.REFRESH_TOKEN_SUCCESS,HttpStatus.OK,response);

        } catch (Exception e) {
            log.error("Lỗi khi làm mới token: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
    }

    public ResponseEntity<ApiResponse<String>> forgotPassword(String email) {
        log.info(" Yêu cầu quên mật khẩu cho email: {}", email);

        if (email == null) {
            log.warn(" Thiếu thông tin email trong yêu cầu forgotPassword");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        ResponseEntity<ApiResponse<String>> response = createPasswordResetTokenForEmail(email);
        log.info(" Kết quả forgotPassword cho {}: {}", email, response.getBody().getMessage());
        return response;
    }

    public ResponseEntity<ApiResponse<String>> resetPassword(Map<String, String> body) {
        log.info(" Yêu cầu reset mật khẩu với dữ liệu: {}", body);

        String token = body.get("token");
        String newPassword = body.get("password");

        if (token == null || newPassword == null) {
            log.warn(" Thiếu token hoặc mật khẩu mới trong resetPassword");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        log.debug(" Token nhận được: {}", token);
        log.debug(" Mật khẩu mới (ẩn log giá trị thực tế để bảo mật)");

        return resetPassword(token, newPassword);
    }

    public ResponseEntity<ApiResponse<String>> createPasswordResetTokenForEmail(String email) {
        log.info(" Yêu cầu reset mật khẩu cho email: {}", email);

        if (email == null) {
            log.warn(" Thiếu thông tin email đầu vào");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        UserEntity userEntity = userService.findUserByEmail(email);
        log.debug(" Tìm thấy user: {}", userEntity != null ? userEntity.getEmail() : "Không tìm thấy");

        if (userEntity == null || "ROLE_ADMIN".equals(userEntity.getRole())) {
            log.warn(" Người dùng không tồn tại hoặc là admin: {}", email);
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.NOT_FOUND,null);
        }

        if(passwordResetTokenRepository.existsByUserEntityId(userEntity.getId())) {
            return ResponseHandler.generateResponse(Messages.PASSWORD_RESET_ALREADY_REQUESTED,HttpStatus.TOO_MANY_REQUESTS,null);
        }

        String token = jwtTokenProvider.generateAccessToken(userEntity);
        log.info(" Token reset password được tạo thành công: {}", token);

        PasswordResetToken passwordResetToken = new PasswordResetToken(userEntity, token, LocalDateTime.now());
        passwordResetTokenRepository.save(passwordResetToken);
        log.info(" Đã lưu PasswordResetToken vào database cho user: {}", email);

        String link = resetUrlPrefix + token;

        String toEmail = email;
        String subject = "Yêu cầu reset mật khẩu";
        String body = "Bạn (hoặc ai đó) đã yêu cầu đặt lại mật khẩu.\n" +
                "Nếu đúng, vui lòng bấm liên kết sau:\n" + link +
                "\nLiên kết hết hạn sau 1 giờ.";

        emailService.sendEmail(toEmail, subject, body);

        log.info(" Đã gửi email reset mật khẩu cho: {}", email);

        return ResponseHandler.generateResponse(Messages.PASSWORD_RESET_REQUEST_SUCCESS,HttpStatus.OK,null);

    }

    public ResponseEntity<ApiResponse<String>> resetPassword(String token, String newPassword) {
        log.info(" Bắt đầu xử lý reset password với token: {}", token);
        PasswordResetToken prt;
        try {
            prt = passwordResetTokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Link đã bị vô hiệu do bạn đã sử dụng trước đó"));

        } catch (RuntimeException e) {
            log.error(" Token không hợp lệ: {}", token);
            return ResponseHandler.generateResponse(Messages.INVALID_INPUT,HttpStatus.BAD_REQUEST,null);
        }


        Date expiration = jwtTokenProvider.getExpirationDate(prt.getToken());
        log.debug(" Token hết hạn vào: {}", expiration);

        if (expiration.before(new Date())) {
            log.warn(" Token đã hết hạn: {}", token);
            passwordResetTokenRepository.delete(prt);
            log.info(" Đã xóa token hết hạn khỏi database");
            return ResponseHandler.generateResponse(Messages.TOKEN_EXPIRED,HttpStatus.GONE,null);
        }

        UserEntity userEntity = prt.getUser();
        log.info(" Đang cập nhật mật khẩu cho user: {}", userEntity.getEmail());

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(userEntity);
        log.info(" Mật khẩu đã được cập nhật thành công cho: {}", userEntity.getEmail());

        passwordResetTokenRepository.delete(prt);
        log.info(" Đã xóa token sau khi reset password thành công");

        return ResponseHandler.generateResponse(Messages.PASSWORD_RESET_SUCCESS,HttpStatus.OK,null);
    }
}
