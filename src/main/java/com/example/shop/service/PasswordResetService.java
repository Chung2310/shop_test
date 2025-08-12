package com.example.shop.service;

import com.example.shop.model.ApiResponse;
import com.example.shop.model.PasswordResetToken;
import com.example.shop.model.User;
import com.example.shop.repository.PasswordResetTokenRepository;
import com.example.shop.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    public ResponseEntity<ApiResponse<String>> createPasswordResetTokenForEmail(String email) {
        log.info("📩 Yêu cầu reset mật khẩu cho email: {}", email);

        if (email == null) {
            log.warn("❌ Thiếu thông tin email đầu vào");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Thiếu thông tin email đầu vào", null)
            );
        }

        User user = userService.findUserByEmail(email);
        log.debug("🔍 Tìm thấy user: {}", user != null ? user.getEmail() : "Không tìm thấy");

        if (user == null || "ROLE_ADMIN".equals(user.getRole())) {
            log.warn("❌ Người dùng không tồn tại hoặc là admin: {}", email);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Người dùng không tồn tại!", null)
            );
        }

        if(passwordResetTokenRepository.existsByUserId(user.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),"Bạn đã yêu cầu reset mật khẩu trước đó. Vui lòng kiểm tra email!",null)
            );
        }

        String token = jwtTokenProvider.generateAccessToken(user);
        log.info("✅ Token reset password được tạo thành công: {}", token);

        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token, LocalDateTime.now());
        passwordResetTokenRepository.save(passwordResetToken);
        log.info("💾 Đã lưu PasswordResetToken vào database cho user: {}", email);

        emailService.sendResetPasswordEmail(user.getEmail(), token);
        log.info("📨 Đã gửi email reset mật khẩu cho: {}", email);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(HttpStatus.OK.value(), "Yêu cầu quên mật khẩu đã được gửi thành công!", null)
        );
    }

    public void resetPassword(String token, String newPassword) {
        log.info("🔑 Bắt đầu xử lý reset password với token: {}", token);

        PasswordResetToken prt = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.error("❌ Token không hợp lệ: {}", token);
                    return new RuntimeException("Token không hợp lệ");
                });

        Date expiration = jwtTokenProvider.getExpirationDate(prt.getToken());
        log.debug("📅 Token hết hạn vào: {}", expiration);

        if (expiration.before(new Date())) {
            log.warn("⚠️ Token đã hết hạn: {}", token);
            passwordResetTokenRepository.delete(prt);
            log.info("🗑️ Đã xóa token hết hạn khỏi database");
            return;
        }

        User user = prt.getUser();
        log.info("👤 Đang cập nhật mật khẩu cho user: {}", user.getEmail());

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);
        log.info("✅ Mật khẩu đã được cập nhật thành công cho: {}", user.getEmail());

        passwordResetTokenRepository.delete(prt);
        log.info("🗑️ Đã xóa token sau khi reset password thành công");
    }
}
