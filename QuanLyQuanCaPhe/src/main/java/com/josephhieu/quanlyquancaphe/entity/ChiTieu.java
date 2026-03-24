package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity đại diện cho bảng 'ChiTieu' (Chi tiêu) trong CSDL.
 * Lưu trữ các khoản chi phí của quán (ví dụ: tiền điện, tiền nước, thuê mặt bằng).
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Entity
@Table(name = "chitieu") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class ChiTieu {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaChiTieu", length = 36, nullable = false)
    private String maChiTieu;

    /**
     * Số tiền đã chi (dạng số thực).
     */
    @Column(name = "SoTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal soTien;

    /**
     * Tên hoặc mô tả của khoản chi (ví dụ: "Tiền điện T10", "Thuê mặt bằng").
     */
    @Column(name = "TenKhoanChi", length = 100)
    private String tenKhoanChi;

    /**
     * Ngày thực hiện chi tiêu.
     */
    @Column(name = "NgayChi", nullable = false)
    private LocalDate ngayChi;

    // --- MỐI QUAN HỆ ---

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) với TaiKhoan.
     * Xác định tài khoản (người) nào đã thực hiện khoản chi này.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiKhoan", nullable = false)
    private TaiKhoan taiKhoan;
}