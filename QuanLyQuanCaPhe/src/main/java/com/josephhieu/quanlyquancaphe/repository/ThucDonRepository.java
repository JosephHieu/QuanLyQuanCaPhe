package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ThucDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link ThucDon}.
 * Chứa các phương thức tìm kiếm và sắp xếp tùy chỉnh cho Thực đơn.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Repository
public interface ThucDonRepository extends JpaRepository<ThucDon, String> { // Entity: ThucDon, Kiểu ID: String (UUID)

    /**
     * Lấy tất cả các món trong thực đơn, sắp xếp theo Loại món (A-Z)
     * rồi đến Tên món (A-Z).
     * Dùng để hiển thị danh sách thực đơn một cách có tổ chức.
     *
     * @return Danh sách ThucDon đã sắp xếp.
     */
    List<ThucDon> findAllByOrderByLoaiMonAscTenMonAsc();

    /**
     * Tìm danh sách Món ăn có Tên chứa một từ khóa (không phân biệt hoa/thường).
     * Dùng cho chức năng "Tìm kiếm danh mục".
     *
     * @param keyword Từ khóa cần tìm kiếm.
     * @return Danh sách ThucDon khớp.
     */
    List<ThucDon> findByTenMonContainingIgnoreCase(String keyword);
}