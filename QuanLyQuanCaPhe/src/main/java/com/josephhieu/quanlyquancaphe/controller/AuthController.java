package com.josephhieu.quanlyquancaphe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu liên quan đến
 * xác thực (Authentication), cụ thể là hiển thị trang đăng nhập.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 * @since 2025-11-03
 */
@Controller
public class AuthController {

    /**
     * Hiển thị trang đăng nhập tùy chỉnh của ứng dụng.
     *
     * Phương thức này được Spring Security gọi (thông qua cấu hình trong SecurityConfig.loginPage())
     * khi một người dùng chưa được xác thực cố gắng truy cập một tài nguyên được bảo vệ,
     * hoặc khi họ truy cập trực tiếp vào URL /login.
     *
     * Xử lý URL: GET /login
     *
     * @return Tên của file view (template) "login"
     * (Tương ứng với file: templates/login.html).
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
