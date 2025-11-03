package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity đại diện cho bảng 'TaiKhoan' (Tài khoản) trong CSDL.
 * Lưu trữ thông tin đăng nhập và quyền hạn của người dùng.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Entity
@Table(name = "taikhoan", uniqueConstraints = {
        // Đảm bảo Tên đăng nhập là duy nhất
        @UniqueConstraint(columnNames = "TenDangNhap")
})
@Getter
@Setter
@NoArgsConstructor
public class TaiKhoan {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaTaiKhoan", length = 36, nullable = false)
    private String maTaiKhoan;

    /**
     * Tên đăng nhập (username) của người dùng. Phải là duy nhất.
     */
    @Column(name = "TenDangNhap", length = 50, nullable = false, unique = true)
    private String tenDangNhap;

    /**
     * Mật khẩu đã được mã hóa (bằng BCrypt) của người dùng.
     */
    @Column(name = "MatKhau", length = 255, nullable = false)
    private String matKhau;

    /**
     * Quyền hạn của tài khoản (ví dụ: "Admin", "Staff").
     * Dùng để Spring Security phân quyền.
     */
    @Column(name = "QuyenHan", length = 50, nullable = false)
    private String quyenHan;

    /**
     * Dữ liệu ảnh đại diện (avatar), lưu dưới dạng BLOB (Binary Large Object).
     */
    @Lob // Đánh dấu là Large Object
    @Column(name = "Anh", columnDefinition = "LONGBLOB") // Chỉ định kiểu CSDL
    private byte[] anh; // Kiểu byte[] trong Java

    // Lưu ý: Mối quan hệ @OneToOne với NhanVien
    // có thể được thêm ở đây (dùng mappedBy) nếu cần truy vấn 2 chiều.
}