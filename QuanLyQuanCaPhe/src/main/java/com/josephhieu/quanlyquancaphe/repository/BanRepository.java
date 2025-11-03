package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link Ban}.
 * Kế thừa JpaRepository để có sẵn các hàm CRUD cơ bản (findById, save, findAll...).
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Repository
public interface BanRepository extends JpaRepository<Ban, String> { // Entity: Ban, Kiểu ID: String (UUID)
    // Spring Data JPA sẽ tự động tạo các phương thức CRUD cơ bản.
    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh ở đây nếu cần, ví dụ:
    // List<Ban> findByTinhTrang(String tinhTrang);
}