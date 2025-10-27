package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {

    // Tự động tìm NhanVien bằng cách join qua bảng TaiKhoan
    Optional<NhanVien> findByTaiKhoan_TenDangNhap(String tenDangNhap);
}
