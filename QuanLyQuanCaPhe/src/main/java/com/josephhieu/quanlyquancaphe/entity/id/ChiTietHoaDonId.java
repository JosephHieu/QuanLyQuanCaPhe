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
@EqualsAndHashCode
public class ChiTietHoaDonId implements Serializable {

    @Column(name = "MaThucDon", length = 36)
    private String maThucDon;

    @Column(name = "MaHoaDon", length = 36)
    private String maHoaDon;
}