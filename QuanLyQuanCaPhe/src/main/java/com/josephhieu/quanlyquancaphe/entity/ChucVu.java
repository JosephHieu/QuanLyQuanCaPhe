package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "chucvu")
@Getter
@Setter
@NoArgsConstructor
public class ChucVu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaChucVu", length = 36, nullable = false)
    private String maChucVu;

    @Column(name = "Luong", nullable = false, precision = 18, scale = 2)
    private BigDecimal luong;

    @Column(name = "TenChucVu", length = 100, nullable = false)
    private String tenChucVu;
}
