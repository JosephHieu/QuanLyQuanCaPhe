package com.josephhieu.quanlyquancaphe.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

/**
 * Lớp ID Phức hợp (Composite Primary Key) cho Entity {@link com.josephhieu.quanlyquancaphe.entity.ChiTietThucDon}.
 *
 * Được sử dụng với annotation {@code @EmbeddedId} trong Entity ChiTietThucDon.
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
public class ChiTietThucDonId implements Serializable {

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.HangHoa} (Nguyên liệu).
     */
    @Column(name = "MaHangHoa", length = 36)
    private String maHangHoa;

    /**
     * Phần khóa ngoại (FK) trỏ đến {@link com.josephhieu.quanlyquancaphe.entity.ThucDon} (Món ăn).
     */
    @Column(name = "MaThucDon", length = 36)
    private String maThucDon;
}