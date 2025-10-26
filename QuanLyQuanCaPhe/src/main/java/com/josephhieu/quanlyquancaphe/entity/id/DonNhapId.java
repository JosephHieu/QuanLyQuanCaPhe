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
public class DonNhapId implements Serializable {

    @Column(name = "MaNhanVien", length = 36)
    private String maNhanVien;

    @Column(name = "MaThietBi", length = 36)
    private String maThietBi;

    @Column(name = "MaHangHoa", length = 36)
    private String maHangHoa;
}