package com.josephhieu.quanlyquancaphe.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

/**
 * Lớp ID Phức hợp (Composite Primary Key) cho Entity {@link com.josephhieu.quanlyquancaphe.entity.DonNhap}.
 *
 * Được sử dụng với annotation {@code @EmbeddedId} trong Entity DonNhap.
 * Lớp này phải implement {@link Serializable} và định nghĩa {@code equals()} và {@code hashCode()}.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode // Bắt buộc cho các lớp ID phức hợp
public class DonNhapId implements Serializable {

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.NhanVien}.
     */
    @Column(name = "MaNhanVien", length = 36)
    private String maNhanVien;

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.ThietBi}.
     * (Thiết kế này giả định mỗi lần nhập hàng/thiết bị đều liên quan đến một thiết bị cụ thể)
     */
    @Column(name = "MaThietBi", length = 36)
    private String maThietBi;

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.HangHoa}.
     */
    @Column(name = "MaHangHoa", length = 36)
    private String maHangHoa;
}