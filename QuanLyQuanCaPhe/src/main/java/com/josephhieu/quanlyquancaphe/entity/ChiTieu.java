package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "chitieu")
@Getter
@Setter
@NoArgsConstructor
public class ChiTieu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaChiTieu", length = 36, nullable = false)
    private String maChiTieu;

    @Column(name = "SoTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal soTien;

    @Column(name = "TenKhoanChi", length = 100)
    private String tenKhoanChi;

    @Column(name = "NgayChi", nullable = false)
    private LocalDate ngayChi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiKhoan", nullable = false)
    private TaiKhoan taiKhoan;
}