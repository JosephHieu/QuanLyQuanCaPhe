package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "donxuat")
@Getter
@Setter
@NoArgsConstructor
public class DonXuat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaDonXuat", length = 36, nullable = false)
    private String maDonXuat;

    @Column(name = "TongTienXuat", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTienXuat;

    @Column(name = "NgayXuat", nullable = false)
    private LocalDate ngayXuat;

    @Column(name = "SoLuong", nullable = false)
    private int soLuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhanVien", nullable = false)
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaHangHoa", nullable = false)
    private HangHoa hangHoa;
}