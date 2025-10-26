package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hoadon")
@Getter
@Setter
@NoArgsConstructor
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaHoaDon", length = 36, nullable = false)
    private String maHoaDon;

    @Column(name = "TongTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "NgayGioTao", nullable = false)
    private LocalDateTime ngayGioTao;

    @Column(name = "TrangThai", nullable = false)
    private boolean trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKhuyenMai") // Cho phép null
    private KhuyenMai khuyenMai;
}