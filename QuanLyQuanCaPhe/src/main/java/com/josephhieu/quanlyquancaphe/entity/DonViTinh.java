package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity đại diện cho bảng 'DonViTinh' (Đơn vị tính) trong CSDL.
 * Lưu trữ các đơn vị đo lường (ví dụ: "kg", "gam", "lít", "cái").
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Entity
@Table(name = "donvitinh") // Tên bảng trong MySQL
@Getter
@Setter
@NoArgsConstructor
public class DonViTinh {

    /**
     * Khóa chính (UUID, VARCHAR(36)).
     * Được tạo tự động bằng chiến lược UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaDonViTinh", length = 36, nullable = false)
    private String maDonViTinh;

    /**
     * Tên của đơn vị (ví dụ: "Kg", "Gam", "Lít").
     */
    @Column(name = "TenDonVi", length = 50, nullable = false)
    private String tenDonVi;

    // Lưu ý: Mối quan hệ @OneToMany với HangHoa
    // có thể được thêm ở đây nếu cần truy vấn 2 chiều.
}