package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ChiTieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// import java.math.BigDecimal; // Import không dùng
import java.time.LocalDate;
import java.util.List;
// import java.util.Map; // Import không dùng

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link ChiTieu}.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Repository
public interface ChiTieuRepository extends JpaRepository<ChiTieu, String> { // Entity: ChiTieu, Kiểu ID: String (UUID)

    /**
     * Tìm TẤT CẢ các khoản chi tiêu nằm trong một khoảng ngày (startDate <= ngayChi <= endDate).
     * Được sử dụng bởi NganSachService (cách 2) để lấy dữ liệu thô cho báo cáo Thu/Chi.
     *
     * @param startDate Ngày bắt đầu.
     * @param endDate   Ngày kết thúc.
     * @return Danh sách các ChiTieu.
     */
    List<ChiTieu> findByNgayChiBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Tìm các khoản chi tiêu gần đây (sau một ngày nhất định).
     * Được sử dụng để hiển thị các khoản chi đã có trong form "Thêm chi tiêu".
     * Sắp xếp theo ngày chi (mới nhất trước).
     *
     * @param startDate Ngày bắt đầu (ví dụ: 7 ngày trước).
     * @return Danh sách các ChiTieu.
     */
    List<ChiTieu> findByNgayChiAfterOrderByNgayChiDesc(LocalDate startDate);
}