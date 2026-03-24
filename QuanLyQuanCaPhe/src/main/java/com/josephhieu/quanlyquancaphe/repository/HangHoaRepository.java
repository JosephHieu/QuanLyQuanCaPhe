package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.HangHoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link HangHoa}.
 * Chứa các phương thức tìm kiếm tùy chỉnh cho nghiệp vụ kho hàng.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Repository
public interface HangHoaRepository extends JpaRepository<HangHoa, String> { // Entity: HangHoa, Kiểu ID: String (UUID)

    /**
     * Tìm một Hàng hóa bằng Tên (không phân biệt hoa/thường).
     * Dùng trong nghiệp vụ "Nhập hàng" để kiểm tra hàng đã tồn tại hay chưa.
     *
     * @param tenHangHoa Tên hàng hóa cần tìm.
     * @return Optional chứa HangHoa nếu tìm thấy.
     */
    Optional<HangHoa> findByTenHangHoaIgnoreCase(String tenHangHoa);

    /**
     * Tìm danh sách Hàng hóa có Tên chứa một từ khóa (không phân biệt hoa/thường).
     * Dùng cho chức năng "Tìm kiếm hàng hóa".
     *
     * @param keyword Từ khóa cần tìm kiếm.
     * @return Danh sách HangHoa khớp.
     */
    List<HangHoa> findByTenHangHoaContainingIgnoreCase(String keyword);

    /**
     * Lấy tất cả Hàng hóa, sắp xếp theo Tên (A-Z).
     * (Đây là phương thức thay thế cho findAll(Sort.by(...)) nếu được dùng thường xuyên).
     *
     * @return Danh sách HangHoa đã sắp xếp.
     */
    List<HangHoa> findAllByOrderByTenHangHoaAsc();

    /**
     * Lấy tất cả Hàng hóa, đồng thời tải (JOIN FETCH) thông tin DonViTinh.
     * Đây là câu query đã được tối ưu hóa để giải quyết vấn đề N+1 Query
     * khi hiển thị danh sách hàng hóa và đơn vị tính, cũng như
     * giải quyết lỗi Lazy Loading khi chuyển đổi sang JSON cho JavaScript.
     *
     * @return Danh sách HangHoa với DonViTinh đã được tải.
     */
    @Query("SELECT h FROM HangHoa h LEFT JOIN FETCH h.donViTinh ORDER BY h.tenHangHoa ASC")
    List<HangHoa> findAllWithDonViTinh();
}