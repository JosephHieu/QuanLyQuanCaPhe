package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.DonViTinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link DonViTinh}.
 * Kế thừa JpaRepository để có sẵn các hàm CRUD cơ bản.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Repository
public interface DonViTinhRepository extends JpaRepository<DonViTinh, String> { // Entity: DonViTinh, Kiểu ID: String (UUID)
    // Các phương thức CRUD cơ bản được JpaRepository cung cấp tự động.
}