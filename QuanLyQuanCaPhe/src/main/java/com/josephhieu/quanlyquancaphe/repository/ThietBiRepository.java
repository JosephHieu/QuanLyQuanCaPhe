package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link ThietBi}.
 * Kế thừa JpaRepository để có sẵn các hàm CRUD cơ bản.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Repository
public interface ThietBiRepository extends JpaRepository<ThietBi, String> { // Entity: ThietBi, Kiểu ID: String (UUID)
    // Các phương thức CRUD cơ bản được JpaRepository cung cấp tự động.
}