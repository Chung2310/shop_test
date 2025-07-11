package com.example.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @Size(max = 20, message = "Số điện thoại tối đa 20 ký tự")
    @Column(length = 20)
    private String phone;

    @Size(max = 255, message = "Địa chỉ quá dài")
    @Column(length = 255)
    private String address;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String role = "user";

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
