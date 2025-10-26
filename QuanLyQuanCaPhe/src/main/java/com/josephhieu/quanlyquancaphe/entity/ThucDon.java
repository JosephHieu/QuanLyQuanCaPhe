package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "thucdon")
@Getter
@Setter
@NoArgsConstructor
public class ThucDon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaThucDon", length = 36, nullable = false)
    private String maThucDon;

    @Column(name = "TenMon", length = 100, nullable = false)
    private String tenMon;

    @Column(name = "GiaTienHienTai", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTienHienTai;

    @Column(name = "LoaiMon", length = 50, nullable = false)
    private String loaiMon;
}