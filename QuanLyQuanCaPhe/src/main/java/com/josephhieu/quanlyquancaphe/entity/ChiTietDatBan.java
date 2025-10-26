package com.josephhieu.quanlyquancaphe.entity;

import com.josephhieu.quanlyquancaphe.entity.id.ChiTietDatBanId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "chitietdatban")
@Getter
@Setter
@NoArgsConstructor
public class ChiTietDatBan {

    // Sử dụng @EmbeddedId thay vì @Id
    @EmbeddedId
    private ChiTietDatBanId id;

    // --- Map các phần của khóa phức hợp thành quan hệ ---
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maBan") // Tên trường trong ChiTietDatBanId
    @JoinColumn(name = "MaBan")
    private Ban ban;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maNhanVien") // Tên trường trong ChiTietDatBanId
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maHoaDon") // Tên trường trong ChiTietDatBanId
    @JoinColumn(name = "MaHoaDon")
    private HoaDon hoaDon;

    // --- Các cột dữ liệu còn lại ---
    @Column(name = "TenKhachHang", length = 100, nullable = false)
    private String tenKhachHang;

    @Column(name = "SdtKhachHang", length = 15)
    private String sdtKhachHang;

    @Column(name = "NgayGioDat", nullable = false)
    private LocalDateTime ngayGioDat;
}