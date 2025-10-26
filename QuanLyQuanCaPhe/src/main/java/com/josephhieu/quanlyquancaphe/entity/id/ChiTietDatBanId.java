package com.josephhieu.quanlyquancaphe.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode // Rất quan trọng cho khóa phức hợp
public class ChiTietDatBanId implements Serializable {

    @Column(name = "MaBan", length = 36)
    private String maBan;

    @Column(name = "MaNhanVien", length = 36)
    private String maNhanVien;

    @Column(name = "MaHoaDon", length = 36)
    private String maHoaDon;
}