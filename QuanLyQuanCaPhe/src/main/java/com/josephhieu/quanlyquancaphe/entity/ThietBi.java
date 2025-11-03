package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity đại diện cho bảng 'ThietBi' (Thiết bị) trong CSDL.
 * Lưu trữ thông tin về tài sản, thiết bị của quán
 * (ví dụ: Máy pha cà phê, Bàn ghế, Tủ lạnh).
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Entity
@Table(name = "thietbi") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class ThietBi {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaThietBi", length = 36, nullable = false)
    private String maThietBi;

    /**
     * Tên của thiết bị (ví dụ: "Máy pha Faema E71").
     */
    @Column(name = "TenThietBi", length = 100, nullable = false)
    private String tenThietBi;

    /**
     * Số lượng thiết bị hiện có.
     */
    @Column(name = "SoLuong", nullable = false)
    private int soLuong;

    /**
     * Ghi chú thêm về thiết bị (tùy chọn).
     */
    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    /**
     * Ngày mua thiết bị.
     */
    @Column(name = "NgayMua", nullable = false)
    private LocalDate ngayMua;

    /**
     * Đơn giá tại thời điểm mua thiết bị (để tính khấu hao).
     */
    @Column(name = "DonGiaMua", nullable = false, precision = 18, scale = 2)
    private BigDecimal donGiaMua;

    // Lưu ý: Mối quan hệ @OneToMany với DonNhap
    // có thể được thêm ở đây nếu cần truy vấn 2 chiều.
}