package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho bảng 'HoaDon' (Hóa đơn).
 * Lưu trữ thông tin chính của mỗi giao dịch bán hàng.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Entity
@Table(name = "hoadon") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class HoaDon {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaHoaDon", length = 36, nullable = false)
    private String maHoaDon;

    /**
     * Tổng số tiền cuối cùng của hóa đơn (có thể đã bao gồm giảm giá).
     */
    @Column(name = "TongTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    /**
     * Ngày và giờ chính xác hóa đơn được tạo (khi bắt đầu đặt bàn/gọi món).
     */
    @Column(name = "NgayGioTao", nullable = false)
    private LocalDateTime ngayGioTao;

    /**
     * Trạng thái thanh toán của hóa đơn.
     * false (0) = Chưa thanh toán (đang hoạt động).
     * true (1) = Đã thanh toán.
     */
    @Column(name = "TrangThai", nullable = false)
    private boolean trangThai;

    // --- MỐI QUAN HỆ ---

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) với KhuyenMai.
     * Cho biết hóa đơn này có áp dụng mã khuyến mãi nào không.
     * {@code @JoinColumn(name = "MaKhuyenMai")} cho phép cột này là NULL
     * (nếu hóa đơn không có khuyến mãi).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKhuyenMai") // Cho phép null
    private KhuyenMai khuyenMai;

    // Lưu ý: Các mối quan hệ @OneToMany với ChiTietDatBan, ChiTietHoaDon
    // có thể được thêm ở đây nếu cần truy vấn 2 chiều.
}