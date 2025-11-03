package com.josephhieu.quanlyquancaphe.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu cho trang chủ (homepage)
 * của vai trò Nhân viên (Staff).
 *
 * @author Joseph Hieu
 * @version 1.0
 * @since 2025-11-03
 */
@Controller
public class StaffController {

    /**
     * Hiển thị trang chủ ("Xin chào, user_one!") dành riêng cho Nhân viên (Staff).
     * Phương thức này được gọi sau khi Staff đăng nhập thành công và được
     * {@link com.josephhieu.quanlyquancaphe.config.CustomAuthenticationSuccessHandler}
     * điều hướng đến đây.
     *
     * Xử lý URL: GET /staff/home
     *
     * @param model Model (cái túi) để truyền dữ liệu (username, currentPage) ra view Thymeleaf.
     * @param userDetails Đối tượng {@link UserDetails} (do Spring Security tự động tiêm vào
     * nhờ @AuthenticationPrincipal) chứa thông tin của Staff đang đăng nhập.
     * @return Tên của file view (template) "home/staff_home"
     * (Tương ứng với file: templates/home/staff_home.html).
     */
    @GetMapping("/staff/home")
    public String staffHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("username", userDetails.getUsername());

        model.addAttribute("currentPage", "home");
        // Trả về file staff_home.html
        return "home/staff_home";
    }
}