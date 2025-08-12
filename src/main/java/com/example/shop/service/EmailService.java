package com.example.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend.reset-url}")
    private String resetUrlPrefix;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public void sendOrderConfirmationEmail(String toEmail, String subject, String body) {
        log.info("📧 Chuẩn bị gửi email xác nhận đơn hàng tới: {}", toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("your-email@gmail.com");

            mailSender.send(message);
            log.info("✅ Đã gửi email xác nhận đơn hàng thành công tới: {}", toEmail);
        } catch (Exception e) {
            log.error("❌ Lỗi khi gửi email xác nhận đơn hàng tới {}: {}", toEmail, e.getMessage(), e);
        }
    }

    public void sendResetPasswordEmail(String email, String token) {
        log.info("📩 Chuẩn bị gửi email reset mật khẩu tới: {}", email);
        try {
            String link = resetUrlPrefix + token;

            String subject = "Yêu cầu reset mật khẩu";
            String content = "Bạn (hoặc ai đó) đã yêu cầu đặt lại mật khẩu.\n" +
                    "Nếu đúng, vui lòng bấm liên kết sau:\n" + link +
                    "\nLiên kết hết hạn sau 1 giờ.";

            log.debug("🔗 Link reset mật khẩu: {}", link);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom("your-email@gmail.com");

            mailSender.send(message);
            log.info("✅ Đã gửi email reset mật khẩu thành công tới: {}", email);
        } catch (Exception e) {
            log.error("❌ Lỗi khi gửi email reset mật khẩu tới {}: {}", email, e.getMessage(), e);
        }
    }
}
