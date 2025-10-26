use quanlyquancaphe;

INSERT INTO
    `chucvu` (`MaChucVu`, `Luong`, `TenChucVu`)
VALUES
    ('CV001', 12000000.00, 'Quản lý'),
    ('CV002', 8000000.00, 'Pha chế trưởng'),
    ('CV003', 7000000.00, 'Thu ngân'),
    ('CV004', 6000000.00, 'Pha chế'),
    ('CV005', 5500000.00, 'Phục vụ'),
    ('CV006', 5000000.00, 'Bảo vệ'),
    ('CV007', 4500000.00, 'Tạp vụ'),
    ('CV008', 9000000.00, 'Phó quản lý'),
    ('CV009', 3000000.00, 'Phục vụ (Part-time)'),
    ('CV010', 2000000.00, 'Thực tập sinh');

-- 2. Chèn dữ liệu cho bảng DonViTinh
INSERT INTO
    `donvitinh` (`MaDonViTinh`, `TenDonVi`)
VALUES
    ('DVT001', 'Kg'),
    ('DVT002', 'Gam'),
    ('DVT003', 'Lít'),
    ('DVT004', 'Ml'),
    ('DVT005', 'Cái'),
    ('DVT006', 'Hộp'),
    ('DVT007', 'Chai'),
    ('DVT008', 'Lon'),
    ('DVT009', 'Gói'),
    ('DVT010', 'Thùng');

-- 3. Chèn dữ liệu cho bảng TaiKhoan
INSERT INTO
    `taikhoan` (
        `MaTaiKhoan`,
        `TenDangNhap`,
        `MatKhau`,
        `QuyenHan`,
        `Anh`
    )
VALUES
    (
        'TK001',
        'admin',
        'hashed_password_123',
        'Admin',
        NULL
    ),
    (
        'TK002',
        'phache01',
        'hashed_password_123',
        'Staff',
        NULL
    ),
    (
        'TK003',
        'thungan01',
        'hashed_password_123',
        'Staff',
        NULL
    ),
    (
        'TK004',
        'phucvu01',
        'hashed_password_123',
        'Staff',
        NULL
    ),
    (
        'TK005',
        'baove01',
        'hashed_password_123',
        'Staff',
        NULL
    ),
    (
        'TK006',
        'tapvu01',
        'hashed_password_123',
        'Staff',
        NULL
    ),
    (
        'TK007',
        'phoql',
        'hashed_password_123',
        'Admin',
        NULL
    ),
    (
        'TK008',
        'phache02',
        'hashed_password_123',
        'Staff',
        NULL
    ),
    (
        'TK009',
        'phucvu02',
        'hashed_password_123',
        'Staff',
        NULL
    ),
    (
        'TK010',
        'thuctap01',
        'hashed_password_123',
        'Staff',
        NULL
    );

-- 4. Chèn dữ liệu cho bảng Ban
INSERT INTO
    `ban` (`MaBan`, `TinhTrang`, `TenBan`)
VALUES
    ('B001', 'Trống', 'Bàn 1'),
    ('B002', 'Trống', 'Bàn 2'),
    ('B003', 'Có khách', 'Bàn 3'),
    ('B004', 'Trống', 'Bàn 4'),
    ('B005', 'Trống', 'Bàn 5'),
    ('B006', 'Có khách', 'Bàn 6'),
    ('B007', 'Trống', 'Bàn 7'),
    ('B008', 'Trống', 'Bàn 8'),
    ('B009', 'Đặt trước', 'Bàn 9'),
    ('B010', 'Trống', 'Bàn 10');

-- 5. Chèn dữ liệu cho bảng KhuyenMai
INSERT INTO
    `khuyenmai` (
        `MaKhuyenMai`,
        `TenKhuyenMai`,
        `NgayBatDau`,
        `NgayKetThuc`,
        `LoaiKhuyenMai`,
        `GiaTriGiam`,
        `TrangThai`,
        `MoTa`
    )
VALUES
    (
        'KM001',
        'Giảm 10% tổng hóa đơn',
        '2025-10-01',
        '2025-10-31',
        'Phần trăm',
        10.00,
        1,
        'Áp dụng cho mọi hóa đơn'
    ),
    (
        'KM002',
        'Mua 2 tặng 1',
        '2025-11-01',
        '2025-11-30',
        'Tặng phẩm',
        0.00,
        1,
        'Áp dụng cho dòng Cafe'
    ),
    (
        'KM003',
        'Giảm 20000 VND',
        '2025-10-20',
        '2025-11-20',
        'Tiền cố định',
        20000.00,
        1,
        'Hóa đơn trên 100000'
    ),
    (
        'KM004',
        'Giáng sinh an lành',
        '2025-12-20',
        '2025-12-25',
        'Phần trăm',
        20.00,
        0,
        'Chưa tới ngày'
    ),
    (
        'KM005',
        'Chào hè',
        '2025-06-01',
        '2025-06-30',
        'Phần trăm',
        15.00,
        0,
        'Đã hết hạn'
    ),
    (
        'KM006',
        'Giờ vàng',
        '2025-10-01',
        '2025-12-31',
        'Phần trăm',
        30.00,
        1,
        'Từ 13h-16h'
    ),
    (
        'KM007',
        'Miễn phí vận chuyển',
        '2025-10-01',
        '2025-10-31',
        'Khác',
        0.00,
        1,
        'Đơn hàng online'
    ),
    (
        'KM008',
        'Đồng giá 29k',
        '2025-11-11',
        '2025-11-11',
        'Khác',
        0.00,
        1,
        'Áp dụng 1 số món'
    ),
    (
        'KM009',
        'Giảm 50% Bánh',
        '2025-10-25',
        '2025-10-30',
        'Phần trăm',
        50.00,
        1,
        'Khi mua kèm nước'
    ),
    (
        'KM010',
        'Tri ân khách hàng',
        '2025-09-01',
        '2025-09-30',
        'Tiền cố định',
        50000.00,
        0,
        'Đã hết hạn'
    );

-- 6. Chèn dữ liệu cho bảng ThietBi
INSERT INTO
    `thietbi` (
        `MaThietBi`,
        `TenThietBi`,
        `SoLuong`,
        `GhiChu`,
        `NgayMua`,
        `DonGiaMua`
    )
VALUES
    (
        'TB001',
        'Máy pha cafe Faema',
        1,
        'Máy 2 họng',
        '2024-01-15',
        80000000.00
    ),
    (
        'TB002',
        'Máy xay cafe Mazzer',
        2,
        NULL,
        '2024-01-15',
        15000000.00
    ),
    (
        'TB003',
        'Tủ lạnh Sanaky',
        2,
        '1 tủ mát, 1 tủ đông',
        '2024-01-20',
        12000000.00
    ),
    (
        'TB004',
        'Máy xay sinh tố Vitamix',
        3,
        NULL,
        '2024-02-01',
        8000000.00
    ),
    (
        'TB005',
        'Lò vi sóng Sharp',
        1,
        'Hâm nóng bánh',
        '2024-02-01',
        2500000.00
    ),
    (
        'TB006',
        'Máy POS bán hàng',
        2,
        NULL,
        '2024-01-10',
        6000000.00
    ),
    (
        'TB007',
        'Camera an ninh',
        8,
        'Hikvision',
        '2024-01-25',
        1200000.00
    ),
    (
        'TB008',
        'Bàn ghế (Bộ)',
        15,
        'Gỗ thông',
        '2024-01-20',
        1500000.00
    ),
    (
        'TB009',
        'Điều hòa Daikin',
        4,
        '2 HP',
        '2024-01-22',
        14000000.00
    ),
    (
        'TB010',
        'Bình đun siêu tốc',
        3,
        NULL,
        '2024-02-05',
        800000.00
    );

-- 7. Chèn dữ liệu cho bảng ThucDon
INSERT INTO
    `thucdon` (
        `MaThucDon`,
        `TenMon`,
        `GiaTienHienTai`,
        `LoaiMon`
    )
VALUES
    ('TD001', 'Cafe Đen', 25000.00, 'Cafe'),
    ('TD002', 'Cafe Sữa', 30000.00, 'Cafe'),
    ('TD003', 'Bạc Xỉu', 35000.00, 'Cafe'),
    ('TD004', 'Trà Đào Cam Sả', 45000.00, 'Trà'),
    ('TD005', 'Trà Vải', 42000.00, 'Trà'),
    ('TD006', 'Nước ép Cam', 40000.00, 'Nước ép'),
    ('TD007', 'Nước ép Ổi', 40000.00, 'Nước ép'),
    ('TD008', 'Sinh tố Bơ', 50000.00, 'Sinh tố'),
    ('TD009', 'Bánh Croissant', 30000.00, 'Bánh'),
    ('TD010', 'Bánh Tiramisu', 35000.00, 'Bánh');

-- ==========================================================
-- BẢNG PHỤ THUỘC (CÓ KHÓA NGOẠI)
-- ==========================================================

-- 8. Chèn dữ liệu cho bảng HangHoa (phụ thuộc DonViTinh)
INSERT INTO
    `hanghoa` (
        `MaHangHoa`,
        `TenHangHoa`,
        `SoLuong`,
        `MaDonViTinh`,
        `DonGia`
    )
VALUES
    ('HH001', 'Hạt cafe Robusta', 50, 'DVT001', 150000.00),
    ('HH002', 'Hạt cafe Arabica', 30, 'DVT001', 250000.00),
    ('HH003', 'Sữa đặc Ngôi Sao', 100, 'DVT008', 20000.00),
    ('HH004', 'Sữa tươi Dalat Milk', 40, 'DVT003', 40000.00),
    ('HH005', 'Đường cát trắng', 80, 'DVT001', 25000.00),
    ('HH006', 'Siro Đào', 20, 'DVT007', 90000.00),
    ('HH007', 'Siro Vải', 20, 'DVT007', 90000.00),
    ('HH008', 'Cam tươi', 15, 'DVT001', 30000.00),
    ('HH009', 'Bánh Croissant (đông lạnh)', 50, 'DVT005', 15000.00),
    ('HH010', 'Bột Cacao', 10, 'DVT001', 120000.00);

-- 9. Chèn dữ liệu cho bảng NhanVien (phụ thuộc ChucVu, TaiKhoan)
INSERT INTO
    `nhanvien` (
        `MaNhanVien`,
        `MaChucVu`,
        `MaTaiKhoan`,
        `HoTen`,
        `SoDienThoai`,
        `DiaChi`
    )
VALUES
    (
        'NV001',
        'CV001',
        'TK001',
        'Nguyễn Văn An',
        '0909111222',
        '123 Lê Lợi, Q1, TPHCM'
    ),
    (
        'NV002',
        'CV002',
        'TK002',
        'Trần Thị Bình',
        '0909222333',
        '456 Lý Thường Kiệt, Q10, TPHCM'
    ),
    (
        'NV003',
        'CV003',
        'TK003',
        'Lê Minh Cường',
        '0909333444',
        '789 CMT8, Q3, TPHCM'
    ),
    (
        'NV004',
        'CV005',
        'TK004',
        'Phạm Thị Dung',
        '0909444555',
        '111 Nguyễn Trãi, Q5, TPHCM'
    ),
    (
        'NV005',
        'CV006',
        'TK005',
        'Võ Văn Em',
        '0909555666',
        '222 Võ Thị Sáu, Q3, TPHCM'
    ),
    (
        'NV006',
        'CV007',
        'TK006',
        'Đặng Thị Giàu',
        '0909666777',
        '333 Hùng Vương, Q5, TPHCM'
    ),
    (
        'NV007',
        'CV008',
        'TK007',
        'Hoàng Văn Hùng',
        '0909777888',
        '444 An Dương Vương, Q5, TPHCM'
    ),
    (
        'NV008',
        'CV004',
        'TK008',
        'Ngô Thị Kim',
        '0909888999',
        '555 Trần Hưng Đạo, Q1, TPHCM'
    ),
    (
        'NV009',
        'CV009',
        'TK009',
        'Lý Văn Long',
        '0909999000',
        '666 Nguyễn Văn Cừ, Q5, TPHCM'
    ),
    (
        'NV010',
        'CV010',
        'TK010',
        'Mai Anh Tuấn',
        '0909101010',
        '777 Sư Vạn Hạnh, Q10, TPHCM'
    );

-- 10. Chèn dữ liệu cho bảng ChiTieu (phụ thuộc TaiKhoan)
-- (Giả sử Quản lý 'TK001' và Phó quản lý 'TK007' thực hiện chi tiêu)
INSERT INTO
    `chitieu` (
        `MaChiTieu`,
        `MaTaiKhoan`,
        `SoTien`,
        `TenKhoanChi`,
        `NgayChi`
    )
VALUES
    (
        'CT001',
        'TK001',
        5000000.00,
        'Trả tiền điện tháng 10',
        '2025-10-05'
    ),
    (
        'CT002',
        'TK001',
        1500000.00,
        'Trả tiền nước tháng 10',
        '2025-10-05'
    ),
    (
        'CT003',
        'TK007',
        300000.00,
        'Mua văn phòng phẩm',
        '2025-10-07'
    ),
    (
        'CT004',
        'TK001',
        20000000.00,
        'Trả tiền thuê mặt bằng',
        '2025-10-01'
    ),
    (
        'CT005',
        'TK007',
        1000000.00,
        'Sửa chữa máy lạnh',
        '2025-10-15'
    ),
    (
        'CT006',
        'TK001',
        10000000.00,
        'Nhập hàng cafe đợt 1',
        '2025-10-02'
    ),
    (
        'CT007',
        'TK001',
        5000000.00,
        'Nhập hàng siro, sữa',
        '2025-10-10'
    ),
    (
        'CT008',
        'TK007',
        700000.00,
        'In ấn menu mới',
        '2025-10-18'
    ),
    (
        'CT009',
        'TK001',
        200000.00,
        'Internet FPT tháng 10',
        '2025-10-06'
    ),
    (
        'CT010',
        'TK001',
        1500000.00,
        'Chi phí quảng cáo Facebook',
        '2025-10-20'
    );

-- 11. Chèn dữ liệu cho bảng HoaDon (phụ thuộc KhuyenMai)
INSERT INTO
    `hoadon` (
        `MaHoaDon`,
        `TongTien`,
        `NgayGioTao`,
        `TrangThai`,
        `MaKhuyenMai`
    )
VALUES
    (
        'HD001',
        65000.00,
        '2025-10-25 08:30:00',
        1,
        NULL
    ),
    (
        'HD002',
        85000.00,
        '2025-10-25 09:15:00',
        1,
        'KM001'
    ),
    (
        'HD003',
        40000.00,
        '2025-10-25 10:05:00',
        1,
        NULL
    ),
    (
        'HD004',
        110000.00,
        '2025-10-25 11:20:00',
        1,
        'KM003'
    ),
    (
        'HD005',
        70000.00,
        '2025-10-25 14:00:00',
        1,
        'KM006'
    ),
    (
        'HD006',
        30000.00,
        '2025-10-26 08:00:00',
        1,
        NULL
    ),
    (
        'HD007',
        75000.00,
        '2025-10-26 09:00:00',
        1,
        NULL
    ),
    (
        'HD008',
        65000.00,
        '2025-10-26 09:30:00',
        1,
        'KM009'
    ),
    (
        'HD009',
        42000.00,
        '2025-10-26 10:10:00',
        0,
        NULL
    ),
    (
        'HD010',
        50000.00,
        '2025-10-26 10:15:00',
        0,
        NULL
    );

-- 12. Chèn dữ liệu cho bảng ChiTietThucDon (phụ thuộc HangHoa, ThucDon)
INSERT INTO
    `chitietthucdon` (
        `MaHangHoa`,
        `MaThucDon`,
        `KhoiLuong`,
        `DonViTinh`
    )
VALUES
    ('HH001', 'TD001', 25.00, 'Gam'),
    ('HH001', 'TD002', 25.00, 'Gam'),
    ('HH003', 'TD002', 40.00, 'Ml'),
    ('HH001', 'TD003', 20.00, 'Gam'),
    ('HH003', 'TD003', 30.00, 'Ml'),
    ('HH004', 'TD003', 30.00, 'Ml'),
    ('HH006', 'TD004', 50.00, 'Ml'),
    ('HH007', 'TD005', 50.00, 'Ml'),
    ('HH008', 'TD006', 0.30, 'Kg'),
    ('HH009', 'TD009', 1.00, 'Cái');

-- 13. Chèn dữ liệu cho bảng DonXuat (phụ thuộc NhanVien, HangHoa)
-- (Giả sử quản lý 'NV001' và phó QL 'NV007' làm đơn xuất kho)
INSERT INTO
    `donxuat` (
        `MaDonXuat`,
        `MaNhanVien`,
        `MaHangHoa`,
        `TongTienXuat`,
        `NgayXuat`,
        `SoLuong`
    )
VALUES
    (
        'DX001',
        'NV001',
        'HH001',
        1500000.00,
        '2025-10-03',
        10
    ),
    (
        'DX002',
        'NV001',
        'HH003',
        400000.00,
        '2025-10-03',
        20
    ),
    (
        'DX003',
        'NV007',
        'HH004',
        800000.00,
        '2025-10-04',
        20
    ),
    (
        'DX004',
        'NV007',
        'HH006',
        450000.00,
        '2025-10-05',
        5
    ),
    (
        'DX005',
        'NV001',
        'HH009',
        150000.00,
        '2025-10-06',
        10
    ),
    (
        'DX006',
        'NV001',
        'HH001',
        1500000.00,
        '2025-10-10',
        10
    ),
    (
        'DX007',
        'NV001',
        'HH003',
        400000.00,
        '2025-10-10',
        20
    ),
    (
        'DX008',
        'NV007',
        'HH008',
        150000.00,
        '2025-10-12',
        5
    ),
    (
        'DX009',
        'NV001',
        'HH002',
        500000.00,
        '2025-10-15',
        2
    ),
    (
        'DX010',
        'NV001',
        'HH005',
        250000.00,
        '2025-10-16',
        10
    );

-- 14. Chèn dữ liệu cho bảng DonNhap (phụ thuộc NhanVien, ThietBi, HangHoa)
-- (Lưu ý: Thiết kế này hơi lạ khi gộp cả 3 mã làm PK, nhưng vẫn chèn theo yêu cầu)
-- (Giả sử quản lý 'NV001' chịu trách nhiệm nhập)
INSERT INTO
    `donnhap` (
        `MaNhanVien`,
        `MaThietBi`,
        `MaHangHoa`,
        `NgayNhap`,
        `TongTien`,
        `SoLuong`
    )
VALUES
    (
        'NV001',
        'TB001',
        'HH001',
        '2025-01-15',
        80000000.00,
        1
    ),
    (
        'NV001',
        'TB002',
        'HH001',
        '2025-01-15',
        15000000.00,
        1
    ),
    (
        'NV001',
        'TB003',
        'HH001',
        '2025-01-20',
        12000000.00,
        1
    ),
    (
        'NV001',
        'TB004',
        'HH001',
        '2025-02-01',
        8000000.00,
        1
    ),
    (
        'NV001',
        'TB001',
        'HH002',
        '2025-10-02',
        7500000.00,
        30
    ),
    (
        'NV001',
        'TB001',
        'HH003',
        '2025-10-02',
        2000000.00,
        100
    ),
    (
        'NV001',
        'TB001',
        'HH004',
        '2025-10-10',
        1600000.00,
        40
    ),
    (
        'NV001',
        'TB001',
        'HH005',
        '2025-10-10',
        2000000.00,
        80
    ),
    (
        'NV001',
        'TB001',
        'HH006',
        '2025-10-10',
        1800000.00,
        20
    ),
    (
        'NV001',
        'TB001',
        'HH009',
        '2025-10-10',
        750000.00,
        50
    );

-- 15. Chèn dữ liệu cho bảng ChiTietHoaDon (phụ thuộc ThucDon, HoaDon)
INSERT INTO
    `chitiethoadon` (
        `MaThucDon`,
        `MaHoaDon`,
        `SoLuong`,
        `GiaTaiThoiDiemBan`,
        `ThanhTien`
    )
VALUES
    ('TD001', 'HD001', 1, 25000.00, 25000.00),
    ('TD002', 'HD001', 1, 30000.00, 30000.00),
    ('TD004', 'HD002', 1, 45000.00, 45000.00),
    ('TD006', 'HD002', 1, 40000.00, 40000.00),
    ('TD006', 'HD003', 1, 40000.00, 40000.00),
    ('TD008', 'HD004', 2, 50000.00, 100000.00),
    ('TD009', 'HD004', 1, 30000.00, 30000.00),
    ('TD003', 'HD005', 2, 35000.00, 70000.00),
    ('TD002', 'HD006', 1, 30000.00, 30000.00),
    ('TD001', 'HD007', 1, 25000.00, 25000.00);

-- 16. Chèn dữ liệu cho bảng ChiTietDatBan (phụ thuộc Ban, NhanVien, HoaDon)
-- (Giả sử nhân viên phục vụ 'NV004' và 'NV009' nhận đặt bàn)
INSERT INTO
    `chitietdatban` (
        `MaBan`,
        `MaNhanVien`,
        `MaHoaDon`,
        `TenKhachHang`,
        `SdtKhachHang`,
        `NgayGioDat`
    )
VALUES
    (
        'B003',
        'NV004',
        'HD001',
        'Anh Hùng',
        '0987654321',
        '2025-10-25 08:00:00'
    ),
    (
        'B006',
        'NV004',
        'HD002',
        'Chị Mai',
        '0912345678',
        '2025-10-25 09:00:00'
    ),
    (
        'B001',
        'NV009',
        'HD003',
        'Anh Tuấn',
        '0911223344',
        '2025-10-25 10:00:00'
    ),
    (
        'B002',
        'NV009',
        'HD004',
        'Chị Lan',
        '0944556677',
        '2025-10-25 11:00:00'
    ),
    (
        'B004',
        'NV004',
        'HD005',
        'Anh Phát',
        '0933445566',
        '2025-10-25 13:30:00'
    ),
    (
        'B005',
        'NV004',
        'HD006',
        'Chị Quyên',
        '0977889900',
        '2025-10-26 07:45:00'
    ),
    (
        'B007',
        'NV009',
        'HD007',
        'Anh Minh',
        '0966554433',
        '2025-10-26 08:30:00'
    ),
    (
        'B008',
        'NV009',
        'HD008',
        'Chị Thảo',
        '0912121212',
        '2025-10-26 09:15:00'
    ),
    (
        'B009',
        'NV004',
        'HD009',
        'Khách vãng lai',
        NULL,
        '2025-10-26 10:10:00'
    ),
    (
        'B010',
        'NV004',
        'HD010',
        'Khách vãng lai',
        NULL,
        '2025-10-26 10:15:00'
    );