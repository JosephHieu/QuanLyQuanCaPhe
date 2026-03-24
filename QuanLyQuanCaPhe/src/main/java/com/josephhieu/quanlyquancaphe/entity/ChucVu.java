package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entity đại diện cho bảng 'ChucVu' (Chức vụ) trong CSDL.
 * Lưu trữ thông tin về các vai trò công việc và mức lương tương ứng.
 * (ví dụ: Quản lý, Pha chế, Phục vụ).
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Entity
@Table(name = "chucvu") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class ChucVu {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaChucVu", length = 36, nullable = false)
    private String maChucVu;

    /**
     * Mức lương cơ bản cho chức vụ này.
     */
    @Column(name = "Luong", nullable = false, precision = 18, scale = 2)
    private BigDecimal luong;

    /**
     * Tên của chức vụ (ví dụ: "Quản lý", "Pha chế").
     * Tên này cũng có thể được dùng để xác định QuyenHan trong TaiKhoan.
     */
    @Column(name = "TenChucVu", length = 100, nullable = false)
    private String tenChucVu;

    // Lưu ý: Mối quan hệ @OneToMany với NhanVien
    // có thể được thêm ở đây nếu cần truy vấn 2 chiều.
}