package com.josephhieu.quanlyquancaphe.entity;

import com.josephhieu.quanlyquancaphe.entity.id.ChiTietThucDonId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "chitietthucdon")
@Getter
@Setter
@NoArgsConstructor
public class ChiTietThucDon {

    @EmbeddedId
    private ChiTietThucDonId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHangHoa")
    @JoinColumn(name = "MaHangHoa")
    private HangHoa hangHoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThucDon")
    @JoinColumn(name = "MaThucDon")
    private ThucDon thucDon;

    @Column(name = "KhoiLuong", nullable = false, precision = 18, scale = 2)
    private BigDecimal khoiLuong;

    @Column(name = "DonViTinh", length = 50)
    private String donViTinh;
}