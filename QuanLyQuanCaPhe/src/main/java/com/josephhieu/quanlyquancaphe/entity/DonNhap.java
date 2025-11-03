package com.josephhieu.quanlyquancaphe.entity;

import com.josephhieu.quanlyquancaphe.entity.id.DonNhapId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity đại diện cho bảng 'DonNhap' (Đơn Nhập).
 * Đây là bảng ghi lại lịch sử các giao dịch nhập kho,
 * liên kết Nhân viên, Thiết bị, và Hàng hóa cho mỗi lần nhập.
 *
 * Entity này sử dụng Khóa chính Phức hợp (Composite Primary Key)
 * được định nghĩa trong lớp {@link DonNhapId}.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Entity
@Table(name = "donnhap") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class DonNhap {

    /**
     * Khóa chính phức hợp, nhúng từ lớp DonNhapId.
     * Bao gồm: maNhanVien, maThietBi, maHangHoa.
     */
    @EmbeddedId
    private DonNhapId id;

    // --- CÁC MỐI QUAN HỆ (ÁNH XẠ TỪ @EmbeddedId) ---

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Nhân viên.
     * {@code @MapsId("maNhanVien")} liên kết trường 'nhanVien' này
     * với thuộc tính 'maNhanVien' trong {@link DonNhapId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maNhanVien")
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Thiết bị.
     * {@code @MapsId("maThietBi")} liên kết trường 'thietBi' này
     * với thuộc tính 'maThietBi' trong {@link DonNhapId}.
     * (Thiết kế này giả định mỗi lần nhập đều liên quan đến 1 thiết bị).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThietBi")
    @JoinColumn(name = "MaThietBi")
    private ThietBi thietBi;

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Hàng hóa (Nguyên liệu).
     * {@code @MapsId("maHangHoa")} liên kết trường 'hangHoa' này
     * với thuộc tính 'maHangHoa' trong {@link DonNhapId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHangHoa")
    @JoinColumn(name = "MaHangHoa")
    private HangHoa hangHoa;

    // --- CÁC CỘT DỮ LIỆU THÔNG THƯỜNG ---

    /**
     * Ngày thực hiện nhập hàng/thiết bị vào kho.
     */
    @Column(name = "NgayNhap", nullable = false)
    private LocalDate ngayNhap;

    /**
     * Tổng giá trị của đơn nhập (Số lượng * Đơn giá tại thời điểm nhập).
     */
    @Column(name = "TongTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    /**
     * Số lượng hàng hóa/thiết bị được nhập.
     */
    @Column(name = "SoLuong", nullable = false)
    private int soLuong;
}