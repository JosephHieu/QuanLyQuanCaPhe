package com.josephhieu.quanlyquancaphe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "taikhoan", uniqueConstraints = {
        @UniqueConstraint(columnNames = "TenDangNhap")
})
@Getter
@Setter
@NoArgsConstructor
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "MaTaiKhoan", length = 36, nullable = false)
    private String maTaiKhoan;

    @Column(name = "TenDangNhap", length = 50, nullable = false, unique = true)
    private String tenDangNhap;

    @Column(name = "MatKhau", length = 255, nullable = false)
    private String matKhau;

    @Column(name = "QuyenHan", length = 50, nullable = false)
    private String quyenHan;

    @Lob
    @Column(name = "Anh", columnDefinition = "LONGBLOB")
    private byte[] anh; // LONGBLOB -> byte[]
}