package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "hanghoa")
@Getter
@Setter
@NoArgsConstructor
public class HangHoa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaHangHoa", length = 36, nullable = false)
    private String maHangHoa;

    @Column(name = "TenHangHoa", length = 100, nullable = false)
    private String tenHangHoa;

    @Column(name = "SoLuong", nullable = false)
    private int soLuong;

    @Column(name = "DonGia", nullable = false, precision = 18, scale = 2)
    private BigDecimal donGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDonViTinh", nullable = false)
    private DonViTinh donViTinh;
}