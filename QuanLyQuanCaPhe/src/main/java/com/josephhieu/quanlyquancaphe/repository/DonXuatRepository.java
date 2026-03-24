package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.DonXuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link DonXuat}.
 * Kế thừa JpaRepository để có sẵn các hàm CRUD cơ bản.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Repository
public interface DonXuatRepository extends JpaRepository<DonXuat, String> { // Entity: DonXuat, Kiểu ID: String (UUID)
    // Các phương thức CRUD cơ bản được JpaRepository cung cấp tự động.
    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh ở đây (ví dụ: tìm theo ngày xuất).
    // List<DonXuat> findByNgayXuatBetween(LocalDate startDate, LocalDate endDate);
}