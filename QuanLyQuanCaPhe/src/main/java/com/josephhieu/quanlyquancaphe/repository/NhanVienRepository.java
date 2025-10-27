package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {

    // Tự động tìm NhanVien bằng cách join qua bảng TaiKhoan
    Optional<NhanVien> findByTaiKhoan_TenDangNhap(String tenDangNhap);

    // Tìm nhân viên có Họ Tên chứa từ khóa (không phân biệt hoa/thường)
    List<NhanVien> findByHoTenContainingIgnoreCase(String keyword);
}
