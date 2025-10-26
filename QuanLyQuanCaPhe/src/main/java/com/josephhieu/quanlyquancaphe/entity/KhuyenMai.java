package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "khuyenmai")
@Getter
@Setter
@NoArgsConstructor
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaKhuyenMai", length = 36, nullable = false)
    private String maKhuyenMai;

    @Column(name = "TenKhuyenMai", length = 100, nullable = false)
    private String tenKhuyenMai;

    @Column(name = "NgayBatDau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "NgayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Column(name = "LoaiKhuyenMai", length = 50, nullable = false)
    private String loaiKhuyenMai;

    @Column(name = "GiaTriGiam", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "TrangThai", nullable = false)
    private boolean trangThai; // TINYINT(1) -> boolean

    @Column(name = "MoTa", length = 255)
    private String moTa;
}