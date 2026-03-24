package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link TaiKhoan}.
 * Chủ yếu dùng cho việc xác thực và quản lý tài khoản.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> { // Entity: TaiKhoan, Kiểu ID: String (UUID)

    /**
     * Tìm một Tài khoản bằng Tên đăng nhập (username).
     * Đây là phương thức cốt lõi được {@link com.josephhieu.quanlyquancaphe.service.CustomUserDetailsService}
     * sử dụng để xác thực người dùng khi đăng nhập.
     *
     * @param tenDangNhap Tên đăng nhập (username) cần tìm.
     * @return Optional chứa TaiKhoan nếu tìm thấy.
     */
    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);
}