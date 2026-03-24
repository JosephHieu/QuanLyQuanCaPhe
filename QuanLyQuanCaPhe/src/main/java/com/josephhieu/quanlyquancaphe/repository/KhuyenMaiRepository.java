package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.KhuyenMai;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link KhuyenMai}.
 * Kế thừa JpaRepository để có sẵn các hàm CRUD cơ bản.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, String> { // Entity: KhuyenMai, Kiểu ID: String (UUID)

    /**
     * Tìm danh sách Khuyến mãi có Tên chứa một từ khóa (không phân biệt hoa/thường).
     * Dùng cho chức năng tìm kiếm.
     *
     * @param keyword Từ khóa cần tìm kiếm.
     * @return Danh sách KhuyenMai khớp.
     */
    List<KhuyenMai> findByTenKhuyenMaiContainingIgnoreCase(String keyword);

    /**
     * Ghi đè (hoặc khai báo lại) phương thức findAll với tham số Sort.
     * Phương thức này đã có sẵn trong JpaRepository, nhưng khai báo lại
     * không gây hại và có thể dùng để làm rõ.
     *
     * @param sort Đối tượng Sort chỉ định tiêu chí sắp xếp.
     * @return Danh sách KhuyenMai đã sắp xếp.
     */
    List<KhuyenMai> findAll(Sort sort);
}