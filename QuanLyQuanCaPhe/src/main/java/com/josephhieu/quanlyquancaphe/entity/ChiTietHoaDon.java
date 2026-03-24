package com.josephhieu.quanlyquancaphe.entity;

import com.josephhieu.quanlyquancaphe.entity.id.ChiTietHoaDonId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Entity đại diện cho bảng liên kết 'ChiTietHoaDon' (Chi tiết Hóa đơn).
 * Đây là bảng "dòng hàng" (line item), lưu trữ từng món ăn
 * và số lượng tương ứng cho một hóa đơn cụ thể.
 *
 * Entity này sử dụng Khóa chính Phức hợp (Composite Primary Key)
 * được định nghĩa trong lớp {@link ChiTietHoaDonId}.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Entity
@Table(name = "chitiethoadon")
@Getter
@Setter
@NoArgsConstructor
public class ChiTietHoaDon {

    /**
     * Khóa chính phức hợp, nhúng từ lớp ChiTietHoaDonId.
     * Bao gồm: maThucDon, maHoaDon.
     */
    @EmbeddedId
    private ChiTietHoaDonId id;

    // --- CÁC MỐI QUAN HỆ (ÁNH XẠ TỪ @EmbeddedId) ---

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Món ăn (ThucDon).
     * {@code @MapsId("maThucDon")} liên kết trường 'thucDon' này
     * với thuộc tính 'maThucDon' trong {@link ChiTietHoaDonId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThucDon")
    @JoinColumn(name = "MaThucDon")
    private ThucDon thucDon;

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Hóa đơn.
     * {@code @MapsId("maHoaDon")} liên kết trường 'hoaDon' này
     * với thuộc tính 'maHoaDon' trong {@link ChiTietHoaDonId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHoaDon")
    @JoinColumn(name = "MaHoaDon")
    private HoaDon hoaDon;

    // --- CÁC CỘT DỮ LIỆU THÔNG THƯỜNG ---

    /**
     * Số lượng món ăn này được gọi trong hóa đơn.
     */
    @Column(name = "SoLuong", nullable = false)
    private int soLuong;

    /**
     * Giá của món ăn TẠI THỜI ĐIỂM bán (lúc gọi món).
     * Dùng để lưu lại giá, phòng trường hợp giá trong bảng ThucDon thay đổi sau này.
     */
    @Column(name = "GiaTaiThoiDiemBan", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTaiThoiDiemBan;

    /**
     * Tổng tiền cho dòng này (SoLuong * GiaTaiThoiDiemBan).
     * Được tính toán và lưu lại để truy vấn tổng tiền hóa đơn nhanh hơn.
     */
    @Column(name = "ThanhTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal thanhTien;
}