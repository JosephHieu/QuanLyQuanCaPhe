use quanlyquancaphe;

DROP TABLE IF EXISTS `Ban`;
CREATE TABLE
    `Ban` (
        `MaBan` VARCHAR(30) NOT NULL,
        `TinhTrang` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `TenBan` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        PRIMARY KEY (`MaBan`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.ChucVu definition
-- Drop table
DROP TABLE IF EXISTS `ChucVu`;
CREATE TABLE
    `ChucVu` (
        `MaChucVu` VARCHAR(30) NOT NULL,
        `Luong` DECIMAL(18, 2) NOT NULL,
        `TenChucVu` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        PRIMARY KEY (`MaChucVu`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.DonViTinh definition
-- Drop table
DROP TABLE IF EXISTS `DonViTinh`;
CREATE TABLE
    `DonViTinh` (
        `MaDonViTinh` VARCHAR(30) NOT NULL,
        `TenDonVi` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        PRIMARY KEY (`MaDonViTinh`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.KhuyenMai definition
-- Drop table
DROP TABLE IF EXISTS `KhuyenMai`;
CREATE TABLE
    `KhuyenMai` (
        `MaKhuyenMai` VARCHAR(30) NOT NULL,
        `TenKhuyenMai` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `NgayBatDau` DATE NOT NULL,
        `NgayKetThuc` DATE NOT NULL,
        `LoaiKhuyenMai` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `GiaTriGiam` DECIMAL(18, 2) NOT NULL,
        `TrangThai` TINYINT(1) NOT NULL,
        `MoTa` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
        PRIMARY KEY (`MaKhuyenMai`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.TaiKhoan definition
-- Drop table
DROP TABLE IF EXISTS `TaiKhoan`;
CREATE TABLE
    `TaiKhoan` (
        `MaTaiKhoan` VARCHAR(30) NOT NULL,
        `TenDangNhap` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `MatKhau` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `QuyenHan` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `Anh` LONGBLOB NULL,
        PRIMARY KEY (`MaTaiKhoan`),
        UNIQUE KEY `UQ__TaiKhoan__55F68FC031F46B83` (`TenDangNhap`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.ThietBi definition
-- Drop table
DROP TABLE IF EXISTS `ThietBi`;
CREATE TABLE
    `ThietBi` (
        `MaThietBi` VARCHAR(30) NOT NULL,
        `TenThietBi` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `SoLuong` INT NOT NULL,
        `GhiChu` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
        `NgayMua` DATE NOT NULL,
        `DonGiaMua` DECIMAL(18, 2) NOT NULL,
        PRIMARY KEY (`MaThietBi`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.ThucDon definition
-- Drop table
DROP TABLE IF EXISTS `ThucDon`;
CREATE TABLE
    `ThucDon` (
        `MaThucDon` VARCHAR(30) NOT NULL,
        `TenMon` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `GiaTienHienTai` DECIMAL(18, 2) NOT NULL,
        `LoaiMon` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        PRIMARY KEY (`MaThucDon`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.ChiTieu definition
-- Drop table
DROP TABLE IF EXISTS `ChiTieu`;
CREATE TABLE
    `ChiTieu` (
        `MaChiTieu` VARCHAR(30) NOT NULL,
        `MaTaiKhoan` VARCHAR(30) NOT NULL,
        `SoTien` DECIMAL(18, 2) NOT NULL,
        `TenKhoanChi` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
        `NgayChi` DATE NOT NULL,
        PRIMARY KEY (`MaChiTieu`),
        CONSTRAINT `FK_ChiTieu_TaiKhoan` FOREIGN KEY (`MaTaiKhoan`) REFERENCES `TaiKhoan` (`MaTaiKhoan`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.HangHoa definition
-- Drop table
DROP TABLE IF EXISTS `HangHoa`;
CREATE TABLE
    `HangHoa` (
        `MaHangHoa` VARCHAR(30) NOT NULL,
        `TenHangHoa` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `SoLuong` INT NOT NULL,
        `MaDonViTinh` VARCHAR(30) NOT NULL,
        `DonGia` DECIMAL(18, 2) NOT NULL,
        PRIMARY KEY (`MaHangHoa`),
        CONSTRAINT `FK_HangHoa_DonViTinh` FOREIGN KEY (`MaDonViTinh`) REFERENCES `DonViTinh` (`MaDonViTinh`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.HoaDon definition
-- Drop table
DROP TABLE IF EXISTS `HoaDon`;
CREATE TABLE
    `HoaDon` (
        `MaHoaDon` VARCHAR(30) NOT NULL,
        `TongTien` DECIMAL(18, 2) NOT NULL,
        `NgayGioTao` DATETIME NOT NULL,
        `TrangThai` TINYINT(1) NOT NULL,
        `MaKhuyenMai` VARCHAR(30) NULL,
        PRIMARY KEY (`MaHoaDon`),
        CONSTRAINT `FK_HoaDon_KhuyenMai` FOREIGN KEY (`MaKhuyenMai`) REFERENCES `KhuyenMai` (`MaKhuyenMai`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.NhanVien definition
-- Drop table
DROP TABLE IF EXISTS `NhanVien`;
CREATE TABLE
    `NhanVien` (
        `MaNhanVien` VARCHAR(30) NOT NULL,
        `MaChucVu` VARCHAR(30) NOT NULL,
        `MaTaiKhoan` VARCHAR(30) NOT NULL,
        `HoTen` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `SoDienThoai` VARCHAR(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
        `DiaChi` VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
        PRIMARY KEY (`MaNhanVien`),
        CONSTRAINT `FK_NhanVien_ChucVu` FOREIGN KEY (`MaChucVu`) REFERENCES `ChucVu` (`MaChucVu`),
        CONSTRAINT `FK_NhanVien_TaiKhoan` FOREIGN KEY (`MaTaiKhoan`) REFERENCES `TaiKhoan` (`MaTaiKhoan`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.ChiTietDatBan definition
-- Drop table
DROP TABLE IF EXISTS `ChiTietDatBan`;
CREATE TABLE
    `ChiTietDatBan` (
        `MaBan` VARCHAR(30) NOT NULL,
        `MaNhanVien` VARCHAR(30) NOT NULL,
        `MaHoaDon` VARCHAR(30) NOT NULL,
        `TenKhachHang` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `SdtKhachHang` VARCHAR(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
        `NgayGioDat` DATETIME NOT NULL,
        PRIMARY KEY (`MaBan`, `MaNhanVien`, `MaHoaDon`),
        CONSTRAINT `FK_CTDatBan_Ban` FOREIGN KEY (`MaBan`) REFERENCES `Ban` (`MaBan`),
        CONSTRAINT `FK_CTDatBan_HoaDon` FOREIGN KEY (`MaHoaDon`) REFERENCES `HoaDon` (`MaHoaDon`),
        CONSTRAINT `FK_CTDatBan_NhanVien` FOREIGN KEY (`MaNhanVien`) REFERENCES `NhanVien` (`MaNhanVien`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.ChiTietHoaDon definition
-- Drop table
DROP TABLE IF EXISTS `ChiTietHoaDon`;
CREATE TABLE
    `ChiTietHoaDon` (
        `MaThucDon` VARCHAR(30) NOT NULL,
        `MaHoaDon` VARCHAR(30) NOT NULL,
        `SoLuong` INT NOT NULL,
        `GiaTaiThoiDiemBan` DECIMAL(18, 2) NOT NULL,
        `ThanhTien` DECIMAL(18, 2) NOT NULL,
        PRIMARY KEY (`MaThucDon`, `MaHoaDon`),
        CONSTRAINT `FK_CTHoaDon_HoaDon` FOREIGN KEY (`MaHoaDon`) REFERENCES `HoaDon` (`MaHoaDon`),
        CONSTRAINT `FK_CTHoaDon_ThucDon` FOREIGN KEY (`MaThucDon`) REFERENCES `ThucDon` (`MaThucDon`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.ChiTietThucDon definition
-- Drop table
DROP TABLE IF EXISTS `ChiTietThucDon`;
CREATE TABLE
    `ChiTietThucDon` (
        `MaHangHoa` VARCHAR(30) NOT NULL,
        `MaThucDon` VARCHAR(30) NOT NULL,
        `KhoiLuong` DECIMAL(18, 2) NOT NULL,
        `DonViTinh` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
        PRIMARY KEY (`MaHangHoa`, `MaThucDon`),
        CONSTRAINT `FK_CTThucDon_HangHoa` FOREIGN KEY (`MaHangHoa`) REFERENCES `HangHoa` (`MaHangHoa`),
        CONSTRAINT `FK_CTThucDon_ThucDon` FOREIGN KEY (`MaThucDon`) REFERENCES `ThucDon` (`MaThucDon`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.DonNhap definition
-- Drop table
DROP TABLE IF EXISTS `DonNhap`;
CREATE TABLE
    `DonNhap` (
        `MaNhanVien` VARCHAR(30) NOT NULL,
        `MaThietBi` VARCHAR(30) NOT NULL,
        `MaHangHoa` VARCHAR(30) NOT NULL,
        `NgayNhap` DATE NOT NULL,
        `TongTien` DECIMAL(18, 2) NOT NULL,
        `SoLuong` INT NOT NULL,
        PRIMARY KEY (`MaNhanVien`, `MaThietBi`, `MaHangHoa`),
        CONSTRAINT `FK_DonNhap_HangHoa` FOREIGN KEY (`MaHangHoa`) REFERENCES `HangHoa` (`MaHangHoa`),
        CONSTRAINT `FK_DonNhap_NhanVien` FOREIGN KEY (`MaNhanVien`) REFERENCES `NhanVien` (`MaNhanVien`),
        CONSTRAINT `FK_DonNhap_ThietBi` FOREIGN KEY (`MaThietBi`) REFERENCES `ThietBi` (`MaThietBi`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- CafeManagement.dbo.DonXuat definition
-- Drop table
DROP TABLE IF EXISTS `DonXuat`;
CREATE TABLE
    `DonXuat` (
        `MaDonXuat` VARCHAR(30) NOT NULL,
        `MaNhanVien` VARCHAR(30) NOT NULL,
        `MaHangHoa` VARCHAR(30) NOT NULL,
        `TongTienXuat` DECIMAL(18, 2) NOT NULL,
        `NgayXuat` DATE NOT NULL,
        `SoLuong` INT NOT NULL,
        PRIMARY KEY (`MaDonXuat`),
        CONSTRAINT `FK_DonXuat_HangHoa` FOREIGN KEY (`MaHangHoa`) REFERENCES `HangHoa` (`MaHangHoa`),
        CONSTRAINT `FK_DonXuat_NhanVien` FOREIGN KEY (`MaNhanVien`) REFERENCES `NhanVien` (`MaNhanVien`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;
    
    