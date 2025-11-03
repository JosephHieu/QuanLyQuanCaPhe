package com.josephhieu.quanlyquancaphe.entity;

import com.josephhieu.quanlyquancaphe.entity.id.ChiTietThucDonId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Entity đại diện cho bảng 'ChiTietThucDon' (Chi tiết Thực đơn).
 * Đây là bảng liên kết (bảng công thức), định nghĩa các nguyên liệu
 * (HangHoa) và khối lượng cần thiết để tạo nên một Món ăn (ThucDon).
 *
 * Entity này sử dụng Khóa chính Phức hợp (Composite Primary Key)
 * được định nghĩa trong lớp {@link ChiTietThucDonId}.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Entity
@Table(name = "chitietthucdon") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class ChiTietThucDon {

    /**
     * Khóa chính phức hợp, nhúng từ lớp ChiTietThucDonId.
     * Bao gồm: maHangHoa, maThucDon.
     */
    @EmbeddedId
    private ChiTietThucDonId id;

    // --- CÁC MỐI QUAN HỆ (ÁNH XẠ TỪ @EmbeddedId) ---

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Hàng hóa (Nguyên liệu).
     * {@code @MapsId("maHangHoa")} liên kết trường 'hangHoa' này
     * với thuộc tính 'maHangHoa' trong {@link ChiTietThucDonId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHangHoa")
    @JoinColumn(name = "MaHangHoa")
    private HangHoa hangHoa;

    /**
     * Mối quan hệ Nhiều-Một (Many-to-One) trỏ đến Món ăn (ThucDon).
     * {@code @MapsId("maThucDon")} liên kết trường 'thucDon' này
     * với thuộc tính 'maThucDon' trong {@link ChiTietThucDonId}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThucDon")
    @JoinColumn(name = "MaThucDon")
    private ThucDon thucDon;

    // --- CÁC CỘT DỮ LIỆU THÔNG THƯỜNG ---

    /**
     * Khối lượng hoặc số lượng nguyên liệu cần thiết cho món ăn.
     * (ví dụ: 5, 10, 0.5)
     */
    @Column(name = "KhoiLuong", nullable = false, precision = 18, scale = 2)
    private BigDecimal khoiLuong;

    /**
     * Tên đơn vị tính tại thời điểm tạo công thức (ví dụ: "gam", "ml").
     * Thường được lấy từ {@link DonViTinh} của Hàng hóa.
     */
    @Column(name = "DonViTinh", length = 50)
    private String donViTinh;
}