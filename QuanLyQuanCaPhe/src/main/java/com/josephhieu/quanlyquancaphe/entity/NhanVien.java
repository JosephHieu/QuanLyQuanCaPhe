package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity đại diện cho bảng 'NhanVien' (Nhân viên) trong CSDL.
 * Lưu trữ thông tin cá nhân của nhân viên.
 * (File này đã được comment ở câu trước, đây là bản lặp lại)
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Entity
@Table(name = "nhanvien") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class NhanVien {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaNhanVien", length = 36, nullable = false)
    private String maNhanVien;

    /**
     * Họ và tên đầy đủ của nhân viên.
     */
    @Column(name = "HoTen", length = 100, nullable = false)
    private String hoTen;

    /**
     * Số điện thoại liên lạc.
     */
    @Column(name = "SoDienThoai", length = 15)
    private String soDienThoai;

    /**
     * Địa chỉ nơi ở.
     */
    @Column(name = "DiaChi", length = 200)
    private String diaChi;

    // --- CÁC MỐI QUAN HỆ ---

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) với ChucVu.
     * Nhiều nhân viên có thể có chung 1 chức vụ.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChucVu", nullable = false)
    private ChucVu chucVu;

    /**
     * Mối quan hệ Một-Một (One-to-One) với TaiKhoan.
     * Mỗi nhân viên chỉ có duy nhất 1 tài khoản đăng nhập.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiKhoan", nullable = false, unique = true)
    private TaiKhoan taiKhoan;

    // Lưu ý: Các mối quan hệ @OneToMany với DonNhap, DonXuat, ChiTietDatBan
    // có thể được thêm ở đây nếu cần truy vấn 2 chiều.
}