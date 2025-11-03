package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Entity đại diện cho bảng 'ThucDon' (Thực đơn / Món ăn).
 * Lưu trữ thông tin cơ bản của một món ăn hoặc đồ uống bán tại quán.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Entity
@Table(name = "thucdon") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class ThucDon {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaThucDon", length = 36, nullable = false)
    private String maThucDon;

    /**
     * Tên món ăn (ví dụ: "Cà phê sữa", "Bánh Croissant").
     */
    @Column(name = "TenMon", length = 100, nullable = false)
    private String tenMon;

    /**
     * Giá bán hiện tại của món ăn.
     */
    @Column(name = "GiaTienHienTai", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTienHienTai;

    /**
     * Phân loại món ăn (ví dụ: "Cafe", "Trà", "Bánh", "Nước ép").
     */
    @Column(name = "LoaiMon", length = 50, nullable = false)
    private String loaiMon;

    // Lưu ý: Các mối quan hệ @OneToMany với ChiTietHoaDon, ChiTietThucDon
    // có thể được thêm ở đây nếu cần truy vấn 2 chiều.
}