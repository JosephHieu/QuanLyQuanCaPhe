package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.dto.ThuChiNgayDTO; // Dường như import này không còn được dùng
import com.josephhieu.quanlyquancaphe.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import này cũng không còn được dùng
import org.springframework.data.repository.query.Param; // Import này cũng không còn được dùng
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link HoaDon}.
 * Kế thừa JpaRepository để có sẵn các hàm CRUD cơ bản.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> { // Entity: HoaDon, Kiểu ID: String (UUID)

    /**
     * Tìm tất cả Hóa đơn có áp dụng một Mã khuyến mãi cụ thể.
     * Dùng để kiểm tra ràng buộc trước khi xóa KhuyenMai.
     *
     * @param maKhuyenMai Mã (UUID) của Khuyến mãi cần tìm.
     * @return Danh sách Hóa đơn khớp.
     */
    List<HoaDon> findByKhuyenMaiMaKhuyenMai(String maKhuyenMai);

    /**
     * Tìm TẤT CẢ hóa đơn (đã thanh toán - trangThai = true)
     * nằm trong một khoảng thời gian (theo NgayGioTao).
     * Được sử dụng bởi NganSachService để lấy dữ liệu thô cho báo cáo Thu.
     *
     * @param startDate Thời điểm bắt đầu (LocalDateTime).
     * @param endDate   Thời điểm kết thúc (LocalDateTime).
     * @return Danh sách Hóa đơn đã thanh toán trong khoảng thời gian.
     */
    List<HoaDon> findByTrangThaiTrueAndNgayGioTaoBetween(LocalDateTime startDate, LocalDateTime endDate);

}