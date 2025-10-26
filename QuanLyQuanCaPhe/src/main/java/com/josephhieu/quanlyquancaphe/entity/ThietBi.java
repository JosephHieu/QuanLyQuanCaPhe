package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "thietbi")
@Getter
@Setter
@NoArgsConstructor
public class ThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaThietBi", length = 36, nullable = false)
    private String maThietBi;

    @Column(name = "TenThietBi", length = 100, nullable = false)
    private String tenThietBi;

    @Column(name = "SoLuong", nullable = false)
    private int soLuong;

    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    @Column(name = "NgayMua", nullable = false)
    private LocalDate ngayMua;

    @Column(name = "DonGiaMua", nullable = false, precision = 18, scale = 2)
    private BigDecimal donGiaMua;
}