package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity đại diện cho bảng 'KhuyenMai' (Khuyến mãi).
 * Lưu trữ thông tin về các chương trình giảm giá, marketing.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Entity
@Table(name = "khuyenmai") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class KhuyenMai {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaKhuyenMai", length = 36, nullable = false)
    private String maKhuyenMai;

    /**
     * Tên của chương trình khuyến mãi (ví dụ: "Chào năm mới", "Giảm 20%").
     */
    @Column(name = "TenKhuyenMai", length = 100, nullable = false)
    private String tenKhuyenMai;

    /**
     * Ngày chương trình bắt đầu có hiệu lực.
     */
    @Column(name = "NgayBatDau", nullable = false)
    private LocalDate ngayBatDau;

    /**
     * Ngày chương trình kết thúc hiệu lực.
     */
    @Column(name = "NgayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;

    /**
     * Loại khuyến mãi (ví dụ: "Phần trăm", "Tiền cố định", "Tặng phẩm").
     */
    @Column(name = "LoaiKhuyenMai", length = 50, nullable = false)
    private String loaiKhuyenMai;

    /**
     * Giá trị của khuyến mãi.
     * Nếu LoaiKhuyenMai là "Phần trăm", đây là % (ví dụ: 20).
     * Nếu LoaiKhuyenMai là "Tiền cố định", đây là số tiền (ví dụ: 20000).
     */
    @Column(name = "GiaTriGiam", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTriGiam;

    /**
     * Trạng thái của khuyến mãi.
     * true (1) = Đang hoạt động, có thể áp dụng.
     * false (0) = Đã hết hạn hoặc bị vô hiệu hóa.
     */
    @Column(name = "TrangThai", nullable = false)
    private boolean trangThai;

    /**
     * Mô tả chi tiết về điều kiện áp dụng (tùy chọn).
     */
    @Column(name = "MoTa", length = 255)
    private String moTa;

    // Lưu ý: Mối quan hệ @OneToMany với HoaDon
    // có thể được thêm ở đây nếu cần truy vấn 2 chiều.
}