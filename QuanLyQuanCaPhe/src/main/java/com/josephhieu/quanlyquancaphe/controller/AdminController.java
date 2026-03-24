package com.josephhieu.quanlyquancaphe.controller;

import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu cho trang chủ (homepage)
 * của vai trò Admin.
 *
 * @author Joseph Hieu
 * @version 1.0
 * @since 2025-11-03
 */
@Controller
public class AdminController {

    /**
     * Hiển thị trang chủ ("Xin chào, admin!") dành riêng cho Admin.
     * Phương thức này được gọi sau khi Admin đăng nhập thành công và được
     * {@link com.josephhieu.quanlyquancaphe.config.CustomAuthenticationSuccessHandler}
     * điều hướng đến đây.
     *
     * Xử lý URL: GET /admin/home
     *
     * @param model Model (cái túi) để truyền dữ liệu (username, currentPage) ra view Thymeleaf.
     * @param userDetails Đối tượng {@link UserDetails} (do Spring Security tự động tiêm vào
     * nhờ @AuthenticationPrincipal) chứa thông tin của Admin đang đăng nhập.
     * @return Tên của file view (template) "home/admin_home"
     * (Tương ứng với file: templates/home/admin_home.html).
     */
    @GetMapping("/admin/home")
    public String adminHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("currentPage", "home");

        return "home/admin_home";
    }
}
