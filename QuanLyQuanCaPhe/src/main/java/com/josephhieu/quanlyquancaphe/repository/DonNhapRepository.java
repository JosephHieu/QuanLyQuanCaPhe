package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.DonNhap;
import com.josephhieu.quanlyquancaphe.entity.id.DonNhapId; // <-- PHẢI IMPORT LỚP ID PHỨC HỢP
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link DonNhap}.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Repository
public interface DonNhapRepository extends JpaRepository<DonNhap, DonNhapId> { // <-- SỬA LẠI KIỂU ID THÀNH DonNhapId
    // Các phương thức CRUD cơ bản (dựa trên ID phức hợp) được JpaRepository cung cấp.
    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh ở đây (ví dụ: tìm theo MaNhanVien).
    // List<DonNhap> findByNhanVienMaNhanVien(String maNhanVien);
}