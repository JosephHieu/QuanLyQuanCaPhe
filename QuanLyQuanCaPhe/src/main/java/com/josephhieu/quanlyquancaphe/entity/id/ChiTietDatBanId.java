package com.josephhieu.quanlyquancaphe.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

/**
 * Lớp ID Phức hợp (Composite Primary Key) cho Entity {@link com.josephhieu.quanlyquancaphe.entity.ChiTietDatBan}.
 *
 * Được sử dụng với annotation {@code @EmbeddedId} trong Entity ChiTietDatBan.
 * Lớp này phải implement {@link Serializable} và định nghĩa {@code equals()} và {@code hashCode()}.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Embeddable // Đánh dấu là một lớp có thể nhúng
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode // Bắt buộc cho các lớp ID phức hợp
public class ChiTietDatBanId implements Serializable {

    // Phải thêm `private static final long serialVersionUID = 1L;` nếu bạn cẩn thận

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.Ban}.
     */
    @Column(name = "MaBan", length = 36)
    private String maBan;

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.NhanVien}.
     */
    @Column(name = "MaNhanVien", length = 36)
    private String maNhanVien;

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.HoaDon}.
     */
    @Column(name = "MaHoaDon", length = 36)
    private String maHoaDon;
}