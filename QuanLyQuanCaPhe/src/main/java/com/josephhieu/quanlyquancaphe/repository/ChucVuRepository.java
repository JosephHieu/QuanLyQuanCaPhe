package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ChucVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link ChucVu}.
 * Kế thừa JpaRepository để có sẵn các hàm CRUD cơ bản (findById, save, findAll...).
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Repository
public interface ChucVuRepository extends JpaRepository<ChucVu, String> { // Entity: ChucVu, Kiểu ID: String (UUID)
    // Các phương thức CRUD cơ bản được JpaRepository cung cấp tự động.
    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh ở đây nếu cần.
}