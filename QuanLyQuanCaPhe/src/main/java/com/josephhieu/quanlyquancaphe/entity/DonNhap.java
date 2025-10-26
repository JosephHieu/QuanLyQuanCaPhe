package com.josephhieu.quanlyquancaphe.entity;

import com.josephhieu.quanlyquancaphe.entity.id.DonNhapId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "donnhap")
@Getter
@Setter
@NoArgsConstructor
public class DonNhap {

    @EmbeddedId
    private DonNhapId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maNhanVien")
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maThietBi")
    @JoinColumn(name = "MaThietBi")
    private ThietBi thietBi;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHangHoa")
    @JoinColumn(name = "MaHangHoa")
    private HangHoa hangHoa;

    @Column(name = "NgayNhap", nullable = false)
    private LocalDate ngayNhap;

    @Column(name = "TongTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "SoLuong", nullable = false)
    private int soLuong;
}