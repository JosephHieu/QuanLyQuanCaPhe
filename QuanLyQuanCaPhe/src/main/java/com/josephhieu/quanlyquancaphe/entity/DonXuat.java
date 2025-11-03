package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity đại diện cho bảng 'DonXuat' (Đơn Xuất).
 * Lưu trữ lịch sử các giao dịch xuất hàng hóa ra khỏi kho
 * (ví dụ: hàng hỏng, hủy, trả lại).
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Entity
@Table(name = "donxuat") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class DonXuat {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaDonXuat", length = 36, nullable = false)
    private String maDonXuat;

    /**
     * Tổng giá trị của đơn xuất tại thời điểm xuất.
     * (Thường là SoLuong * DonGia của HangHoa tại lúc đó).
     */
    @Column(name = "TongTienXuat", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTienXuat;

    /**
     * Ngày thực hiện xuất hàng.
     */
    @Column(name = "NgayXuat", nullable = false)
    private LocalDate ngayXuat;

    /**
     * Số lượng hàng hóa đã xuất.
     */
    @Column(name = "SoLuong", nullable = false)
    private int soLuong;

    // --- MỐI QUAN HỆ ---

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) với NhanVien.
     * Xác định nhân viên nào chịu trách nhiệm cho đơn xuất này.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhanVien", nullable = false)
    private NhanVien nhanVien;

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) với HangHoa.
     * Xác định mặt hàng nào đã được xuất.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaHangHoa", nullable = false)
    private HangHoa hangHoa;
}