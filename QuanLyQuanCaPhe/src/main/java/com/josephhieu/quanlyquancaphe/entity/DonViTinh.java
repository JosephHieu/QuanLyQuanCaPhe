package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "donvitinh")
@Getter
@Setter
@NoArgsConstructor
public class DonViTinh {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaDonViTinh", length = 36, nullable = false)
    private String maDonViTinh;

    @Column(name = "TenDonVi", length = 50, nullable = false)
    private String tenDonVi;
}