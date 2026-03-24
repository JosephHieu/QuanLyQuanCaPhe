package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ChiTietThucDon;
// import com.josephhieu.quanlyquancaphe.entity.ThucDon; // Import này không cần thiết
import com.josephhieu.quanlyquancaphe.entity.id.ChiTietThucDonId; // <-- PHẢI IMPORT LỚP ID
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link ChiTietThucDon} (công thức món ăn).
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Repository
public interface ChiTietThucDonRepository extends JpaRepository<ChiTietThucDon, ChiTietThucDonId> { // <-- SỬA LẠI KIỂU ID THÀNH ChiTietThucDonId

    /**
     * Xóa tất cả các thành phần (nguyên liệu) thuộc về một Món ăn (ThucDon).
     * Dùng trong nghiệp vụ "Sửa món" hoặc "Xóa món".
     * Cần được gọi bên trong một phương thức @Transactional.
     *
     * @param maThucDon Mã (UUID) của Thực đơn.
     */
    void deleteAllByThucDonMaThucDon(String maThucDon);

    /**
     * Tìm tất cả các thành phần (nguyên liệu) thuộc về một Món ăn (ThucDon).
     * Dùng để hiển thị công thức trong form "Sửa món".
     *
     * @param maThucDon Mã (UUID) của Thực đơn.
     * @return Danh sách các chi tiết thực đơn (thành phần).
     */
    List<ChiTietThucDon> findAllByThucDonMaThucDon(String maThucDon);
}