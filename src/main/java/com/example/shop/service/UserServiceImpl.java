package com.example.shop.service;

import com.example.shop.dto.UserDTO;
import com.example.shop.dto.mapper.UserMapper;
import com.example.shop.model.ApiReponse;
import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;
import com.example.shop.security.JwtUtil;
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

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService,UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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


    public ResponseEntity<ApiReponse<UserDTO>> createUser(User user) {
        log.info(" [createUser] Bắt đầu tạo tài khoản mới cho email: {}", user.getEmail());

        if (userRepository.findByEmail(user.getEmail()) != null) {
            log.warn(" [createUser] Email đã tồn tại trong hệ thống: {}", user.getEmail());
            return ResponseEntity.ok(
                    new ApiReponse<>(HttpStatus.CONFLICT.value(), "Tài khoản đã tồn tại!", null)
            );
        }

        try {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User createdUser = userRepository.save(user);

            UserDTO userDTO = userMapper.toDto(createdUser);

            log.info(" [createUser] Tạo tài khoản thành công với ID: {}", createdUser.getId());

            return ResponseEntity.ok(
                    new ApiReponse<>(HttpStatus.CREATED.value(), "Tạo người dùng thành công!", userDTO)
            );
        } catch (Exception e) {
            log.error(" [createUser] Lỗi khi tạo tài khoản: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiReponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đăng ký không thành công do lỗi hệ thống!", null)
            );
        }
    }
}
