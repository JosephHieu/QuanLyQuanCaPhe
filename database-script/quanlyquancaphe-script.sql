-- ==========================================================
-- BƯỚC 1: XÓA VÀ TẠO LẠI CƠ SỞ DỮ LIỆU
-- ==========================================================
DROP DATABASE IF EXISTS quanlyquancaphe;
CREATE DATABASE quanlyquancaphe;
USE quanlyquancaphe;

-- ==========================================================
-- BƯỚC 2: TẠO LẠI CÁC BẢNG VỚI VARCHAR(36)
-- ==========================================================

-- Bảng độc lập
CREATE TABLE Ban (
    MaBan VARCHAR(36) NOT NULL,
    TinhTrang VARCHAR(50) NOT NULL,
    TenBan VARCHAR(50) NOT NULL,
    PRIMARY KEY (MaBan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE ChucVu (
    MaChucVu VARCHAR(36) NOT NULL,
    Luong DECIMAL(18, 2) NOT NULL,
    TenChucVu VARCHAR(100) NOT NULL,
    PRIMARY KEY (MaChucVu)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE DonViTinh (
    MaDonViTinh VARCHAR(36) NOT NULL,
    TenDonVi VARCHAR(50) NOT NULL,
    PRIMARY KEY (MaDonViTinh)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE KhuyenMai (
    MaKhuyenMai VARCHAR(36) NOT NULL,
    TenKhuyenMai VARCHAR(100) NOT NULL,
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    LoaiKhuyenMai VARCHAR(50) NOT NULL,
    GiaTriGiam DECIMAL(18, 2) NOT NULL,
    TrangThai TINYINT(1) NOT NULL,
    MoTa VARCHAR(255) NULL,
    PRIMARY KEY (MaKhuyenMai)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE TaiKhoan (
    MaTaiKhoan VARCHAR(36) NOT NULL,
    TenDangNhap VARCHAR(50) NOT NULL UNIQUE,
    MatKhau VARCHAR(255) NOT NULL,
    QuyenHan VARCHAR(50) NOT NULL,
    Anh LONGBLOB NULL,
    PRIMARY KEY (MaTaiKhoan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE ThietBi (
    MaThietBi VARCHAR(36) NOT NULL,
    TenThietBi VARCHAR(100) NOT NULL,
    SoLuong INT NOT NULL,
    GhiChu VARCHAR(255) NULL,
    NgayMua DATE NOT NULL,
    DonGiaMua DECIMAL(18, 2) NOT NULL,
    PRIMARY KEY (MaThietBi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE ThucDon (
    MaThucDon VARCHAR(36) NOT NULL,
    TenMon VARCHAR(100) NOT NULL,
    GiaTienHienTai DECIMAL(18, 2) NOT NULL,
    LoaiMon VARCHAR(50) NOT NULL,
    PRIMARY KEY (MaThucDon)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Bảng phụ thuộc (có FK)
CREATE TABLE ChiTieu (
    MaChiTieu VARCHAR(36) NOT NULL,
    MaTaiKhoan VARCHAR(36) NOT NULL,
    SoTien DECIMAL(18, 2) NOT NULL,
    TenKhoanChi VARCHAR(100) NULL,
    NgayChi DATE NOT NULL,
    PRIMARY KEY (MaChiTieu),
    CONSTRAINT FK_ChiTieu_TaiKhoan FOREIGN KEY (MaTaiKhoan) REFERENCES TaiKhoan (MaTaiKhoan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE HangHoa (
    MaHangHoa VARCHAR(36) NOT NULL,
    TenHangHoa VARCHAR(100) NOT NULL,
    SoLuong INT NOT NULL,
    MaDonViTinh VARCHAR(36) NOT NULL,
    DonGia DECIMAL(18, 2) NOT NULL,
    PRIMARY KEY (MaHangHoa),
    CONSTRAINT FK_HangHoa_DonViTinh FOREIGN KEY (MaDonViTinh) REFERENCES DonViTinh (MaDonViTinh)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE HoaDon (
    MaHoaDon VARCHAR(36) NOT NULL,
    TongTien DECIMAL(18, 2) NOT NULL,
    NgayGioTao DATETIME NOT NULL,
    TrangThai TINYINT(1) NOT NULL,
    MaKhuyenMai VARCHAR(36) NULL,
    PRIMARY KEY (MaHoaDon),
    CONSTRAINT FK_HoaDon_KhuyenMai FOREIGN KEY (MaKhuyenMai) REFERENCES KhuyenMai (MaKhuyenMai)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE NhanVien (
    MaNhanVien VARCHAR(36) NOT NULL,
    MaChucVu VARCHAR(36) NOT NULL,
    MaTaiKhoan VARCHAR(36) NOT NULL UNIQUE, -- Đảm bảo mỗi NV chỉ có 1 TK
    HoTen VARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(15) NULL,
    DiaChi VARCHAR(200) NULL,
    PRIMARY KEY (MaNhanVien),
    CONSTRAINT FK_NhanVien_ChucVu FOREIGN KEY (MaChucVu) REFERENCES ChucVu (MaChucVu),
    CONSTRAINT FK_NhanVien_TaiKhoan FOREIGN KEY (MaTaiKhoan) REFERENCES TaiKhoan (MaTaiKhoan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE DonXuat (
    MaDonXuat VARCHAR(36) NOT NULL,
    MaNhanVien VARCHAR(36) NOT NULL,
    MaHangHoa VARCHAR(36) NOT NULL,
    TongTienXuat DECIMAL(18, 2) NOT NULL,
    NgayXuat DATE NOT NULL,
    SoLuong INT NOT NULL,
    PRIMARY KEY (MaDonXuat),
    CONSTRAINT FK_DonXuat_NhanVien FOREIGN KEY (MaNhanVien) REFERENCES NhanVien (MaNhanVien),
    CONSTRAINT FK_DonXuat_HangHoa FOREIGN KEY (MaHangHoa) REFERENCES HangHoa (MaHangHoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Bảng liên kết (khóa phức hợp)
CREATE TABLE ChiTietDatBan (
    MaBan VARCHAR(36) NOT NULL,
    MaNhanVien VARCHAR(36) NOT NULL,
    MaHoaDon VARCHAR(36) NOT NULL,
    TenKhachHang VARCHAR(100) NOT NULL,
    SdtKhachHang VARCHAR(15) NULL,
    NgayGioDat DATETIME NOT NULL,
    PRIMARY KEY (MaBan, MaNhanVien, MaHoaDon),
    CONSTRAINT FK_CTDatBan_Ban FOREIGN KEY (MaBan) REFERENCES Ban (MaBan),
    CONSTRAINT FK_CTDatBan_NhanVien FOREIGN KEY (MaNhanVien) REFERENCES NhanVien (MaNhanVien),
    CONSTRAINT FK_CTDatBan_HoaDon FOREIGN KEY (MaHoaDon) REFERENCES HoaDon (MaHoaDon)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE ChiTietHoaDon (
    MaThucDon VARCHAR(36) NOT NULL,
    MaHoaDon VARCHAR(36) NOT NULL,
    SoLuong INT NOT NULL,
    GiaTaiThoiDiemBan DECIMAL(18, 2) NOT NULL,
    ThanhTien DECIMAL(18, 2) NOT NULL,
    PRIMARY KEY (MaThucDon, MaHoaDon),
    CONSTRAINT FK_CTHoaDon_ThucDon FOREIGN KEY (MaThucDon) REFERENCES ThucDon (MaThucDon),
    CONSTRAINT FK_CTHoaDon_HoaDon FOREIGN KEY (MaHoaDon) REFERENCES HoaDon (MaHoaDon)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE ChiTietThucDon (
    MaHangHoa VARCHAR(36) NOT NULL,
    MaThucDon VARCHAR(36) NOT NULL,
    KhoiLuong DECIMAL(18, 2) NOT NULL,
    DonViTinh VARCHAR(50) NULL,
    PRIMARY KEY (MaHangHoa, MaThucDon),
    CONSTRAINT FK_CTThucDon_HangHoa FOREIGN KEY (MaHangHoa) REFERENCES HangHoa (MaHangHoa),
    CONSTRAINT FK_CTThucDon_ThucDon FOREIGN KEY (MaThucDon) REFERENCES ThucDon (MaThucDon)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE DonNhap (
    MaNhanVien VARCHAR(36) NOT NULL,
    MaThietBi VARCHAR(36) NOT NULL,
    MaHangHoa VARCHAR(36) NOT NULL,
    NgayNhap DATE NOT NULL,
    TongTien DECIMAL(18, 2) NOT NULL,
    SoLuong INT NOT NULL,
    PRIMARY KEY (MaNhanVien, MaThietBi, MaHangHoa),
    CONSTRAINT FK_DonNhap_NhanVien FOREIGN KEY (MaNhanVien) REFERENCES NhanVien (MaNhanVien),
    CONSTRAINT FK_DonNhap_ThietBi FOREIGN KEY (MaThietBi) REFERENCES ThietBi (MaThietBi),
    CONSTRAINT FK_DonNhap_HangHoa FOREIGN KEY (MaHangHoa) REFERENCES HangHoa (MaHangHoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ==========================================================
-- BƯỚC 3: THÊM DỮ LIỆU MẪU (SỬ DỤNG UUID())
-- ==========================================================

-- Lưu ý: Chúng ta cần lưu UUID vào biến để sử dụng lại cho khóa ngoại.

-- 1. Bảng ChucVu
SET @cv_ql = UUID(); SET @cv_pct = UUID(); SET @cv_tn = UUID(); SET @cv_pc = UUID(); SET @cv_pv = UUID();
SET @cv_bv = UUID(); SET @cv_tv = UUID(); SET @cv_pql = UUID(); SET @cv_pvpt = UUID(); SET @cv_tts = UUID();
INSERT INTO ChucVu (MaChucVu, Luong, TenChucVu) VALUES
(@cv_ql, 12000000.00, 'Quản lý'), (@cv_pct, 8000000.00, 'Pha chế trưởng'), (@cv_tn, 7000000.00, 'Thu ngân'),
(@cv_pc, 6000000.00, 'Pha chế'), (@cv_pv, 5500000.00, 'Phục vụ'), (@cv_bv, 5000000.00, 'Bảo vệ'),
(@cv_tv, 4500000.00, 'Tạp vụ'), (@cv_pql, 9000000.00, 'Phó quản lý'), (@cv_pvpt, 3000000.00, 'Phục vụ (Part-time)'),
(@cv_tts, 2000000.00, 'Thực tập sinh');

-- 2. Bảng DonViTinh
SET @dvt_kg = UUID(); SET @dvt_g = UUID(); SET @dvt_l = UUID(); SET @dvt_ml = UUID(); SET @dvt_cai = UUID();
SET @dvt_hop = UUID(); SET @dvt_chai = UUID(); SET @dvt_lon = UUID(); SET @dvt_goi = UUID(); SET @dvt_thung = UUID();
INSERT INTO DonViTinh (MaDonViTinh, TenDonVi) VALUES
(@dvt_kg, 'Kg'), (@dvt_g, 'Gam'), (@dvt_l, 'Lít'), (@dvt_ml, 'Ml'), (@dvt_cai, 'Cái'),
(@dvt_hop, 'Hộp'), (@dvt_chai, 'Chai'), (@dvt_lon, 'Lon'), (@dvt_goi, 'Gói'), (@dvt_thung, 'Thùng');

-- 3. Bảng TaiKhoan (Sử dụng mật khẩu đã mã hóa BCrypt cho '123')
SET @tk_admin = UUID(); SET @tk_phache01 = UUID(); SET @tk_thungan01 = UUID(); SET @tk_phucvu01 = UUID(); SET @tk_baove01 = UUID();
SET @tk_tapvu01 = UUID(); SET @tk_phoql = UUID(); SET @tk_phache02 = UUID(); SET @tk_phucvu02 = UUID(); SET @tk_thuctap01 = UUID();
SET @hashed_pw = '$2a$10$3/q.w5y.1nE.8.e.E...'; -- **Thay bằng mã hash BCrypt của '123' bạn tạo ra**
INSERT INTO TaiKhoan (MaTaiKhoan, TenDangNhap, MatKhau, QuyenHan, Anh) VALUES
(@tk_admin, 'admin', @hashed_pw, 'Admin', NULL), (@tk_phache01, 'phache01', @hashed_pw, 'Staff', NULL),
(@tk_thungan01, 'thungan01', @hashed_pw, 'Staff', NULL), (@tk_phucvu01, 'phucvu01', @hashed_pw, 'Staff', NULL),
(@tk_baove01, 'baove01', @hashed_pw, 'Staff', NULL), (@tk_tapvu01, 'tapvu01', @hashed_pw, 'Staff', NULL),
(@tk_phoql, 'phoql', @hashed_pw, 'Admin', NULL), (@tk_phache02, 'phache02', @hashed_pw, 'Staff', NULL),
(@tk_phucvu02, 'phucvu02', @hashed_pw, 'Staff', NULL), (@tk_thuctap01, 'thuctap01', @hashed_pw, 'Staff', NULL);

-- 4. Bảng Ban
SET @b1=UUID(); SET @b2=UUID(); SET @b3=UUID(); SET @b4=UUID(); SET @b5=UUID();
SET @b6=UUID(); SET @b7=UUID(); SET @b8=UUID(); SET @b9=UUID(); SET @b10=UUID();
INSERT INTO Ban (MaBan, TinhTrang, TenBan) VALUES
(@b1, 'Trống', 'Bàn 1'), (@b2, 'Trống', 'Bàn 2'), (@b3, 'Có khách', 'Bàn 3'), (@b4, 'Trống', 'Bàn 4'), (@b5, 'Trống', 'Bàn 5'),
(@b6, 'Có khách', 'Bàn 6'), (@b7, 'Trống', 'Bàn 7'), (@b8, 'Trống', 'Bàn 8'), (@b9, 'Đặt trước', 'Bàn 9'), (@b10, 'Trống', 'Bàn 10');

-- 5. Bảng KhuyenMai
SET @km1=UUID(); SET @km2=UUID(); SET @km3=UUID(); SET @km4=UUID(); SET @km5=UUID();
SET @km6=UUID(); SET @km7=UUID(); SET @km8=UUID(); SET @km9=UUID(); SET @km10=UUID();
INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, NgayBatDau, NgayKetThuc, LoaiKhuyenMai, GiaTriGiam, TrangThai, MoTa) VALUES
(@km1, 'Giảm 10%', '2025-10-01', '2025-10-31', 'Phần trăm', 10.00, 1, 'Mọi hóa đơn'),
(@km2, 'Mua 2 tặng 1', '2025-11-01', '2025-11-30', 'Tặng phẩm', 0.00, 1, 'Dòng Cafe'),
(@km3, 'Giảm 20k', '2025-10-20', '2025-11-20', 'Tiền cố định', 20000.00, 1, 'Hóa đơn > 100k'),
(@km4, 'Giáng sinh', '2025-12-20', '2025-12-25', 'Phần trăm', 20.00, 0, 'Chưa tới ngày'),
(@km5, 'Chào hè', '2025-06-01', '2025-06-30', 'Phần trăm', 15.00, 0, 'Hết hạn'),
(@km6, 'Giờ vàng', '2025-10-01', '2025-12-31', 'Phần trăm', 30.00, 1, '13h-16h'),
(@km7, 'Free ship', '2025-10-01', '2025-10-31', 'Khác', 0.00, 1, 'Online'),
(@km8, 'Đồng giá 29k', '2025-11-11', '2025-11-11', 'Khác', 0.00, 1, 'Một số món'),
(@km9, 'Giảm 50% Bánh', '2025-10-25', '2025-10-30', 'Phần trăm', 50.00, 1, 'Kèm nước'),
(@km10, 'Tri ân', '2025-09-01', '2025-09-30', 'Tiền cố định', 50000.00, 0, 'Hết hạn');

-- 6. Bảng ThietBi
SET @tb1=UUID(); SET @tb2=UUID(); SET @tb3=UUID(); SET @tb4=UUID(); SET @tb5=UUID();
SET @tb6=UUID(); SET @tb7=UUID(); SET @tb8=UUID(); SET @tb9=UUID(); SET @tb10=UUID();
INSERT INTO ThietBi (MaThietBi, TenThietBi, SoLuong, GhiChu, NgayMua, DonGiaMua) VALUES
(@tb1, 'Máy pha Faema', 1, '2 họng', '2024-01-15', 80000000.00), (@tb2, 'Máy xay Mazzer', 2, NULL, '2024-01-15', 15000000.00),
(@tb3, 'Tủ lạnh Sanaky', 2, '1 mát, 1 đông', '2024-01-20', 12000000.00), (@tb4, 'Máy xay Vitamix', 3, NULL, '2024-02-01', 8000000.00),
(@tb5, 'Lò vi sóng Sharp', 1, 'Hâm bánh', '2024-02-01', 2500000.00), (@tb6, 'Máy POS', 2, NULL, '2024-01-10', 6000000.00),
(@tb7, 'Camera', 8, 'Hikvision', '2024-01-25', 1200000.00), (@tb8, 'Bàn ghế (Bộ)', 15, 'Gỗ thông', '2024-01-20', 1500000.00),
(@tb9, 'Điều hòa Daikin', 4, '2 HP', '2024-01-22', 14000000.00), (@tb10, 'Bình đun', 3, NULL, '2024-02-05', 800000.00);

-- 7. Bảng ThucDon
SET @td1=UUID(); SET @td2=UUID(); SET @td3=UUID(); SET @td4=UUID(); SET @td5=UUID();
SET @td6=UUID(); SET @td7=UUID(); SET @td8=UUID(); SET @td9=UUID(); SET @td10=UUID();
INSERT INTO ThucDon (MaThucDon, TenMon, GiaTienHienTai, LoaiMon) VALUES
(@td1, 'Cafe Đen', 25000.00, 'Cafe'), (@td2, 'Cafe Sữa', 30000.00, 'Cafe'), (@td3, 'Bạc Xỉu', 35000.00, 'Cafe'),
(@td4, 'Trà Đào Cam Sả', 45000.00, 'Trà'), (@td5, 'Trà Vải', 42000.00, 'Trà'), (@td6, 'Nước ép Cam', 40000.00, 'Nước ép'),
(@td7, 'Nước ép Ổi', 40000.00, 'Nước ép'), (@td8, 'Sinh tố Bơ', 50000.00, 'Sinh tố'), (@td9, 'Bánh Croissant', 30000.00, 'Bánh'),
(@td10, 'Bánh Tiramisu', 35000.00, 'Bánh');

-- 8. Bảng HangHoa (FK: DonViTinh)
SET @hh1=UUID(); SET @hh2=UUID(); SET @hh3=UUID(); SET @hh4=UUID(); SET @hh5=UUID();
SET @hh6=UUID(); SET @hh7=UUID(); SET @hh8=UUID(); SET @hh9=UUID(); SET @hh10=UUID();
INSERT INTO HangHoa (MaHangHoa, TenHangHoa, SoLuong, MaDonViTinh, DonGia) VALUES
(@hh1, 'Hạt cafe Robusta', 50, @dvt_kg, 150000.00), (@hh2, 'Hạt cafe Arabica', 30, @dvt_kg, 250000.00),
(@hh3, 'Sữa đặc Ngôi Sao', 100, @dvt_lon, 20000.00), (@hh4, 'Sữa tươi Dalat Milk', 40, @dvt_l, 40000.00),
(@hh5, 'Đường cát trắng', 80, @dvt_kg, 25000.00), (@hh6, 'Siro Đào', 20, @dvt_chai, 90000.00),
(@hh7, 'Siro Vải', 20, @dvt_chai, 90000.00), (@hh8, 'Cam tươi', 15, @dvt_kg, 30000.00),
(@hh9, 'Bánh Croissant (đông lạnh)', 50, @dvt_cai, 15000.00), (@hh10, 'Bột Cacao', 10, @dvt_kg, 120000.00);

-- 9. Bảng NhanVien (FK: ChucVu, TaiKhoan)
SET @nv1=UUID(); SET @nv2=UUID(); SET @nv3=UUID(); SET @nv4=UUID(); SET @nv5=UUID();
SET @nv6=UUID(); SET @nv7=UUID(); SET @nv8=UUID(); SET @nv9=UUID(); SET @nv10=UUID();
INSERT INTO NhanVien (MaNhanVien, MaChucVu, MaTaiKhoan, HoTen, SoDienThoai, DiaChi) VALUES
(@nv1, @cv_ql, @tk_admin, 'Nguyễn Văn An', '0909111222', '123 Lê Lợi, Q1'),
(@nv2, @cv_pct, @tk_phache01, 'Trần Thị Bình', '0909222333', '456 Lý Thường Kiệt, Q10'),
(@nv3, @cv_tn, @tk_thungan01, 'Lê Minh Cường', '0909333444', '789 CMT8, Q3'),
(@nv4, @cv_pv, @tk_phucvu01, 'Phạm Thị Dung', '0909444555', '111 Nguyễn Trãi, Q5'),
(@nv5, @cv_bv, @tk_baove01, 'Võ Văn Em', '0909555666', '222 Võ Thị Sáu, Q3'),
(@nv6, @cv_tv, @tk_tapvu01, 'Đặng Thị Giàu', '0909666777', '333 Hùng Vương, Q5'),
(@nv7, @cv_pql, @tk_phoql, 'Hoàng Văn Hùng', '0909777888', '444 An Dương Vương, Q5'),
(@nv8, @cv_pc, @tk_phache02, 'Ngô Thị Kim', '0909888999', '555 Trần Hưng Đạo, Q1'),
(@nv9, @cv_pvpt, @tk_phucvu02, 'Lý Văn Long', '0909999000', '666 Nguyễn Văn Cừ, Q5'),
(@nv10, @cv_tts, @tk_thuctap01, 'Mai Anh Tuấn', '0909101010', '777 Sư Vạn Hạnh, Q10');

-- 10. Bảng ChiTieu (FK: TaiKhoan)
INSERT INTO ChiTieu (MaChiTieu, MaTaiKhoan, SoTien, TenKhoanChi, NgayChi) VALUES
(UUID(), @tk_admin, 5000000.00, 'Tiền điện T10', '2025-10-05'), (UUID(), @tk_admin, 1500000.00, 'Tiền nước T10', '2025-10-05'),
(UUID(), @tk_phoql, 300000.00, 'Văn phòng phẩm', '2025-10-07'), (UUID(), @tk_admin, 20000000.00, 'Thuê mặt bằng', '2025-10-01'),
(UUID(), @tk_phoql, 1000000.00, 'Sửa máy lạnh', '2025-10-15'), (UUID(), @tk_admin, 10000000.00, 'Nhập cafe đợt 1', '2025-10-02'),
(UUID(), @tk_admin, 5000000.00, 'Nhập siro, sữa', '2025-10-10'), (UUID(), @tk_phoql, 700000.00, 'In menu', '2025-10-18'),
(UUID(), @tk_admin, 200000.00, 'Internet T10', '2025-10-06'), (UUID(), @tk_admin, 1500000.00, 'Quảng cáo FB', '2025-10-20');

-- 11. Bảng HoaDon (FK: KhuyenMai)
SET @hd1=UUID(); SET @hd2=UUID(); SET @hd3=UUID(); SET @hd4=UUID(); SET @hd5=UUID();
SET @hd6=UUID(); SET @hd7=UUID(); SET @hd8=UUID(); SET @hd9=UUID(); SET @hd10=UUID();
INSERT INTO HoaDon (MaHoaDon, TongTien, NgayGioTao, TrangThai, MaKhuyenMai) VALUES
(@hd1, 65000.00, '2025-10-25 08:30:00', 1, NULL), (@hd2, 76500.00, '2025-10-25 09:15:00', 1, @km1), -- KM 10%
(@hd3, 40000.00, '2025-10-25 10:05:00', 1, NULL), (@hd4, 110000.00, '2025-10-25 11:20:00', 1, @km3), -- KM 20k
(@hd5, 49000.00, '2025-10-25 14:00:00', 1, @km6), -- KM 30%
(@hd6, 30000.00, '2025-10-26 08:00:00', 1, NULL), (@hd7, 75000.00, '2025-10-26 09:00:00', 1, NULL),
(@hd8, 50000.00, '2025-10-26 09:30:00', 1, @km9), -- KM 50% Bánh (Giả sử tổng bill là 65k, giảm 15k)
(@hd9, 42000.00, '2025-10-26 10:10:00', 0, NULL), (@hd10, 50000.00, '2025-10-26 10:15:00', 0, NULL);

-- 12. Bảng ChiTietThucDon (FK: HangHoa, ThucDon)
INSERT INTO ChiTietThucDon (MaHangHoa, MaThucDon, KhoiLuong, DonViTinh) VALUES
(@hh1, @td1, 25.00, 'Gam'), (@hh1, @td2, 25.00, 'Gam'), (@hh3, @td2, 40.00, 'Ml'),
(@hh1, @td3, 20.00, 'Gam'), (@hh3, @td3, 30.00, 'Ml'), (@hh4, @td3, 30.00, 'Ml'),
(@hh6, @td4, 50.00, 'Ml'), (@hh7, @td5, 50.00, 'Ml'), (@hh8, @td6, 0.30, 'Kg'),
(@hh9, @td9, 1.00, 'Cái'); -- Giả sử TD9 chỉ cần 1 cái bánh

-- 13. Bảng DonXuat (FK: NhanVien, HangHoa)
INSERT INTO DonXuat (MaDonXuat, MaNhanVien, MaHangHoa, TongTienXuat, NgayXuat, SoLuong) VALUES
(UUID(), @nv1, @hh1, 1500000.00, '2025-10-03', 10), (UUID(), @nv1, @hh3, 400000.00, '2025-10-03', 20),
(UUID(), @nv7, @hh4, 800000.00, '2025-10-04', 20), (UUID(), @nv7, @hh6, 450000.00, '2025-10-05', 5),
(UUID(), @nv1, @hh9, 150000.00, '2025-10-06', 10), (UUID(), @nv1, @hh1, 1500000.00, '2025-10-10', 10),
(UUID(), @nv1, @hh3, 400000.00, '2025-10-10', 20), (UUID(), @nv7, @hh8, 150000.00, '2025-10-12', 5),
(UUID(), @nv1, @hh2, 500000.00, '2025-10-15', 2), (UUID(), @nv1, @hh5, 250000.00, '2025-10-16', 10);

-- 14. Bảng DonNhap (FK: NhanVien, ThietBi, HangHoa)
INSERT INTO DonNhap (MaNhanVien, MaThietBi, MaHangHoa, NgayNhap, TongTien, SoLuong) VALUES
-- Giả sử nhập hàng hóa, không phải thiết bị
(@nv1, @tb1, @hh1, '2025-10-02', 3000000.00, 20), (@nv1, @tb1, @hh2, '2025-10-02', 2500000.00, 10),
(@nv7, @tb1, @hh3, '2025-10-02', 1000000.00, 50), (@nv7, @tb1, @hh4, '2025-10-10', 800000.00, 20),
(@nv1, @tb1, @hh5, '2025-10-10', 1250000.00, 50), (@nv1, @tb1, @hh6, '2025-10-10', 900000.00, 10),
(@nv1, @tb1, @hh7, '2025-10-10', 900000.00, 10), (@nv7, @tb1, @hh8, '2025-10-11', 300000.00, 10),
(@nv1, @tb1, @hh9, '2025-10-11', 450000.00, 30), (@nv1, @tb1, @hh10, '2025-10-11', 600000.00, 5);
-- Lưu ý: Khóa phức hợp này vẫn hơi lạ khi nhập hàng lại cần mã thiết bị

-- 15. Bảng ChiTietHoaDon (FK: ThucDon, HoaDon)
INSERT INTO ChiTietHoaDon (MaThucDon, MaHoaDon, SoLuong, GiaTaiThoiDiemBan, ThanhTien) VALUES
(@td1, @hd1, 1, 25000.00, 25000.00), (@td2, @hd1, 1, 30000.00, 30000.00),
(@td4, @hd2, 1, 45000.00, 45000.00), (@td6, @hd2, 1, 40000.00, 40000.00),
(@td6, @hd3, 1, 40000.00, 40000.00), (@td8, @hd4, 2, 50000.00, 100000.00), (@td9, @hd4, 1, 30000.00, 30000.00),
(@td3, @hd5, 2, 35000.00, 70000.00), (@td2, @hd6, 1, 30000.00, 30000.00),
(@td1, @hd7, 1, 25000.00, 25000.00); -- Thêm CTHD cho @hd7

-- 16. Bảng ChiTietDatBan (FK: Ban, NhanVien, HoaDon)
INSERT INTO ChiTietDatBan (MaBan, MaNhanVien, MaHoaDon, TenKhachHang, SdtKhachHang, NgayGioDat) VALUES
(@b3, @nv4, @hd1, 'Anh Hùng', '0987654321', '2025-10-25 08:00:00'),
(@b6, @nv4, @hd2, 'Chị Mai', '0912345678', '2025-10-25 09:00:00'),
(@b1, @nv9, @hd3, 'Anh Tuấn', '0911223344', '2025-10-25 10:00:00'),
(@b2, @nv9, @hd4, 'Chị Lan', '0944556677', '2025-10-25 11:00:00'),
(@b4, @nv4, @hd5, 'Anh Phát', '0933445566', '2025-10-25 13:30:00'),
(@b5, @nv4, @hd6, 'Chị Quyên', '0977889900', '2025-10-26 07:45:00'),
(@b7, @nv9, @hd7, 'Anh Minh', '0966554433', '2025-10-26 08:30:00'),
(@b8, @nv9, @hd8, 'Chị Thảo', '0912121212', '2025-10-26 09:15:00'),
(@b9, @nv4, @hd9, 'Khách đặt', NULL, '2025-10-26 10:10:00'), -- HD9 chưa thanh toán
(@b10, @nv4, @hd10, 'Khách đặt', NULL, '2025-10-26 10:15:00'); -- HD10 chưa thanh toán