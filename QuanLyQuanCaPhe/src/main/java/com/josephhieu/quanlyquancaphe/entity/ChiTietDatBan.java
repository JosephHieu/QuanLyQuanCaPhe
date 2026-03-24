package com.josephhieu.quanlyquancaphe.entity;

import com.josephhieu.quanlyquancaphe.entity.id.ChiTietDatBanId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho bảng liên kết 'ChiTietDatBan'.
 * Bảng này liên kết Bàn, Nhân viên, và Hóa đơn cho một phiên đặt bàn
 * (bao gồm cả thông tin khách hàng và thời gian đặt).
 *
 * Entity này sử dụng một Khóa chính Phức hợp (Composite Primary Key)
 * được định nghĩa trong lớp {@link ChiTietDatBanId}.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Entity
@Table(name = "chitietdatban")
@Getter
@Setter
@NoArgsConstructor
public class ChiTietDatBan {

    /**
     * Khóa chính phức hợp, nhúng từ lớp ChiTietDatBanId.
     * Bao gồm: maBan, maNhanVien, maHoaDon.
     */
    @EmbeddedId
    private ChiTietDatBanId id;

    // --- CÁC MỐI QUAN HỆ (ÁNH XẠ TỪ @EmbeddedId) ---

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Bàn.
     * {@code @MapsId("maBan")} chỉ định rằng trường 'ban' này
     * liên kết với thuộc tính 'maBan' trong {@link ChiTietDatBanId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maBan")
    @JoinColumn(name = "MaBan")
    private Ban ban;

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Nhân viên.
     * {@code @MapsId("maNhanVien")} chỉ định rằng trường 'nhanVien' này
     * liên kết với thuộc tính 'maNhanVien' trong {@link ChiTietDatBanId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maNhanVien")
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Hóa đơn.
     * {@code @MapsId("maHoaDon")} chỉ định rằng trường 'hoaDon' này
     * liên kết với thuộc tính 'maHoaDon' trong {@link ChiTietDatBanId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHoaDon")
    @JoinColumn(name = "MaHoaDon")
    private HoaDon hoaDon;

    // --- CÁC CỘT DỮ LIỆU THÔNG THƯỜNG ---

    /**
     * Tên của khách hàng đặt bàn (ví dụ: "Anh Hùng", "Chị Mai").
     */
    @Column(name = "TenKhachHang", length = 100, nullable = false)
    private String tenKhachHang;

    /**
     * Số điện thoại của khách hàng (tùy chọn, có thể null).
     */
    @Column(name = "SdtKhachHang", length = 15)
    private String sdtKhachHang;

    /**
     * Thời điểm khách hàng đặt bàn hoặc thời điểm hẹn đến (cho nghiệp vụ "Đặt trước").
     */
    @Column(name = "NgayGioDat", nullable = false)
    private LocalDateTime ngayGioDat;
}