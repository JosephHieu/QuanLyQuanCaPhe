package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Entity đại diện cho bảng 'HangHoa' (Hàng hóa).
 * Lưu trữ thông tin về các nguyên vật liệu, hàng hóa trong kho
 * (ví dụ: Hạt cà phê, Sữa tươi, Bánh ngọt...).
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Entity
@Table(name = "hanghoa") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class HangHoa {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaHangHoa", length = 36, nullable = false)
    private String maHangHoa;

    /**
     * Tên của hàng hóa (ví dụ: "Hạt cafe Robusta", "Siro Đào").
     */
    @Column(name = "TenHangHoa", length = 100, nullable = false)
    private String tenHangHoa;

    /**
     * Số lượng tồn kho hiện tại của hàng hóa.
     * Sẽ được cập nhật (tăng/giảm) bởi các nghiệp vụ Nhập/Xuất kho.
     */
    @Column(name = "SoLuong", nullable = false)
    private int soLuong;

    /**
     * Đơn giá hiện tại của hàng hóa (có thể là giá nhập cuối cùng).
     */
    @Column(name = "DonGia", nullable = false, precision = 18, scale = 2)
    private BigDecimal donGia;

    // --- MỐI QUAN HỆ ---

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) với DonViTinh.
     * Xác định đơn vị của hàng hóa (ví dụ: "kg", "lít", "cái").
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDonViTinh", nullable = false)
    private DonViTinh donViTinh;

    // Lưu ý: Các mối quan hệ @OneToMany với DonNhap, DonXuat, ChiTietThucDon
    // có thể được thêm ở đây nếu cần truy vấn 2 chiều.
}