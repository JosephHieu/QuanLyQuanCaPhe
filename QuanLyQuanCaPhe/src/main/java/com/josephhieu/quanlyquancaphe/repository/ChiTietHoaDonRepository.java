package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ChiTietHoaDon;
import com.josephhieu.quanlyquancaphe.entity.HoaDon;
import com.josephhieu.quanlyquancaphe.entity.id.ChiTietHoaDonId; // <-- PHẢI IMPORT LỚP ID
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link ChiTietHoaDon}.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, ChiTietHoaDonId> { // <-- SỬA LẠI KIỂU ID THÀNH ChiTietHoaDonId

    /**
     * Tìm tất cả các món ăn (ChiTietHoaDon) thuộc về một Hóa đơn cụ thể.
     *
     * @param maHoaDon Mã (UUID) của Hóa đơn.
     * @return Danh sách các chi tiết hóa đơn.
     */
    List<ChiTietHoaDon> findByHoaDonMaHoaDon(String maHoaDon);

    /**
     * Xóa tất cả ChiTietHoaDon thuộc về một danh sách các Hóa đơn.
     * Dùng trong nghiệp vụ "Gộp bàn" (để xóa món ăn của các hóa đơn nguồn).
     * Cần được gọi bên trong một phương thức @Transactional.
     *
     * @param hoaDons Danh sách các đối tượng HoaDon cần xóa chi tiết.
     */
    void deleteAllByHoaDonIn(List<HoaDon> hoaDons);

    /**
     * Kiểm tra xem có bất kỳ ChiTietHoaDon nào liên kết với một MaThucDon cụ thể không.
     * Dùng để ngăn ngừa việc xóa một Món ăn (ThucDon) đã từng được bán.
     *
     * @param maThucDon Mã (UUID) của Thực đơn cần kiểm tra.
     * @return true nếu tồn tại, false nếu không.
     */
    boolean existsByThucDonMaThucDon(String maThucDon);
}