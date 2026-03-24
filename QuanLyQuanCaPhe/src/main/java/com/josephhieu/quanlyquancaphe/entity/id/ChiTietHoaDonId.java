package com.josephhieu.quanlyquancaphe.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

/**
 * Lớp ID Phức hợp (Composite Primary Key) cho Entity {@link com.josephhieu.quanlyquancaphe.entity.ChiTietHoaDon}.
 *
 * Được sử dụng với annotation {@code @EmbeddedId} trong Entity ChiTietHoaDon.
 * Lớp này phải implement {@link Serializable} và định nghĩa {@code equals()} và {@code hashCode()}.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode // Bắt buộc cho các lớp ID phức hợp
public class ChiTietHoaDonId implements Serializable {

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.ThucDon}.
     */
    @Column(name = "MaThucDon", length = 36)
    private String maThucDon;

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.HoaDon}.
     */
    @Column(name = "MaHoaDon", length = 36)
    private String maHoaDon;
}