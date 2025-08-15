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



    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public void sendEmail(String toEmail, String subject, String body) {
        log.info(" Chuẩn bị gửi email xác nhận đơn hàng tới: {}", toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("your-email@gmail.com");

            mailSender.send(message);
            log.info(" Đã gửi email xác nhận đơn hàng thành công tới: {}", toEmail);
        } catch (Exception e) {
            log.error(" Lỗi khi gửi email xác nhận đơn hàng tới {}: {}", toEmail, e.getMessage(), e);
        }
    }

}
