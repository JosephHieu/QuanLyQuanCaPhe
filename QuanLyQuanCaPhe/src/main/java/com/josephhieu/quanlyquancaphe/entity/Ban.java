package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity đại diện cho bảng 'Ban' (Bàn) trong CSDL.
 * Lưu trữ thông tin về từng bàn trong quán cà phê.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Entity
@Table(name = "ban") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class Ban {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaBan", length = 36, nullable = false)
    private String maBan;

    /**
     * Tên hiển thị của bàn (ví dụ: "Bàn 1", "Bàn 02", "Ban công").
     */
    @Column(name = "TenBan", length = 50, nullable = false)
    private String tenBan;

    /**
     * Trạng thái hiện tại của bàn (ví dụ: "Trống", "Có khách", "Đặt trước").
     * Dùng để hiển thị màu sắc và logic nghiệp vụ trên trang Quản lý Bán hàng.
     */
    @Column(name = "TinhTrang", length = 50, nullable = false)
    private String tinhTrang;

    // Lưu ý: Mối quan hệ @OneToMany với ChiTietDatBan
    // có thể được thêm ở đây nếu cần truy vấn 2 chiều (từ Bàn -> Chi tiết)
    // @OneToMany(mappedBy = "ban")
    // private Set<ChiTietDatBan> chiTietDatBans;
}