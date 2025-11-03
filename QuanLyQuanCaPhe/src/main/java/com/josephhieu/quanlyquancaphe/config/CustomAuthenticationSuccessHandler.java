package com.josephhieu.quanlyquancaphe.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Set;

/**
 * Lớp tùy chỉnh để xử lý logic SAU KHI đăng nhập thành công.
 * Nhiệm vụ chính là điều hướng (redirect) người dùng đến
 * trang chủ (homepage) chính xác dựa trên quyền (Role) của họ.
 *
 * @author Joseph Hieu
 * @version 1.0
 * @since 2025-11-03
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Phương thức này được Spring Security tự động gọi
     * ngay sau khi xác thực tên đăng nhập và mật khẩu thành công.
     *
     * @param request        Đối tượng HttpServletRequest (chứa thông tin yêu cầu)
     * @param response       Đối tượng HttpServletResponse (dùng để điều hướng)
     * @param authentication Đối tượng Authentication (chứa thông tin người dùng,
     * quan trọng nhất là danh sách quyền)
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1. Lấy danh sách các quyền (roles) của người dùng vừa đăng nhập.
        // Ví dụ: ["ROLE_ADMIN"], ["ROLE_STAFF"]
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // 2. Kiểm tra quyền và điều hướng
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/home");
        } else if (roles.contains("ROLE_STAFF")) {
            response.sendRedirect("/staff/home");
        } else {
            response.sendRedirect("/login");
        }

    }
}
