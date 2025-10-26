package com.josephhieu.quanlyquancaphe.entity;

import com.josephhieu.quanlyquancaphe.entity.id.ChiTietHoaDonId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "chitiethoadon")
@Getter
@Setter
@NoArgsConstructor
public class ChiTietHoaDon {

    @EmbeddedId
    private ChiTietHoaDonId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThucDon")
    @JoinColumn(name = "MaThucDon")
    private ThucDon thucDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHoaDon")
    @JoinColumn(name = "MaHoaDon")
    private HoaDon hoaDon;

    @Column(name = "SoLuong", nullable = false)
    private int soLuong;

    @Column(name = "GiaTaiThoiDiemBan", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTaiThoiDiemBan;

    @Column(name = "ThanhTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal thanhTien;
}