package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.dto.ChiTieuDTO;
import com.josephhieu.quanlyquancaphe.dto.ThuChiNgayDTO;
import com.josephhieu.quanlyquancaphe.dto.TongThuChiDTO;
import com.josephhieu.quanlyquancaphe.entity.ChiTieu; // Import
import com.josephhieu.quanlyquancaphe.entity.HoaDon; // Import
import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import com.josephhieu.quanlyquancaphe.entity.TaiKhoan;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.repository.ChiTieuRepository;
import com.josephhieu.quanlyquancaphe.repository.HoaDonRepository;
import com.josephhieu.quanlyquancaphe.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors; // Import

@Service
public class NganSachService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTieuRepository chiTieuRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Transactional(readOnly = true) // Vẫn nên dùng Transactional
    public TongThuChiDTO getTongHopThuChi(LocalDate startDate, LocalDate endDate) {

        // 1. Lấy danh sách thô Hóa đơn (Thu)
        List<HoaDon> danhSachThu = hoaDonRepository.findByTrangThaiTrueAndNgayGioTaoBetween(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay() // +1 ngày để bao gồm cả ngày kết thúc
        );

        // 2. Lấy danh sách thô Chi tiêu (Chi)
        List<ChiTieu> danhSachChi = chiTieuRepository.findByNgayChiBetween(startDate, endDate);

        // 3. Dùng Java Streams để Nhóm (Group By) và Tính tổng (Sum)

        // Nhóm Hóa đơn theo Ngày và tính tổng TongTien
        Map<LocalDate, BigDecimal> mapThu = danhSachThu.stream()
                .collect(Collectors.groupingBy(
                        hd -> hd.getNgayGioTao().toLocalDate(), // Nhóm theo ngày
                        TreeMap::new, // Dùng TreeMap để tự sắp xếp ngày
                        Collectors.reducing(BigDecimal.ZERO, HoaDon::getTongTien, BigDecimal::add) // Tính tổng
                ));

        // Nhóm Chi tiêu theo Ngày và tính tổng SoTien
        Map<LocalDate, BigDecimal> mapChi = danhSachChi.stream()
                .collect(Collectors.groupingBy(
                        ChiTieu::getNgayChi, // Nhóm theo ngày
                        TreeMap::new, // Dùng TreeMap
                        Collectors.reducing(BigDecimal.ZERO, ChiTieu::getSoTien, BigDecimal::add) // Tính tổng
                ));

        // 4. Gộp 2 Map (Thu và Chi) lại
        Map<LocalDate, ThuChiNgayDTO> mapTongHop = new TreeMap<>();

        // Thêm Thu vào Map tổng hợp
        mapThu.forEach((ngay, tongThu) -> {
            mapTongHop.put(ngay, new ThuChiNgayDTO(ngay, tongThu, BigDecimal.ZERO));
        });

        // Thêm Chi vào Map tổng hợp
        mapChi.forEach((ngay, tongChi) -> {
            // Kiểm tra xem ngày này đã có (từ Thu) chưa
            ThuChiNgayDTO ngayDTO = mapTongHop.get(ngay);
            if (ngayDTO != null) {
                ngayDTO.setTongChi(tongChi); // Cập nhật Chi
            } else {
                mapTongHop.put(ngay, new ThuChiNgayDTO(ngay, BigDecimal.ZERO, tongChi)); // Thêm mới
            }
        });

        // 5. Tính toán tổng cộng
        BigDecimal tongThuCong = mapThu.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tongChiCong = mapChi.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        // 6. Trả về DTO tổng hợp
        return new TongThuChiDTO(
                mapTongHop.values().stream().collect(Collectors.toList()), // Chuyển Map về List
                tongThuCong,
                tongChiCong
        );
    }

    /**
     * PHƯƠNG THỨC MỚI: Lưu (Thêm/Sửa) nhiều khoản chi
     */
    @Transactional
    public void saveChiTieuList(List<ChiTieuDTO> dtos, String tenDangNhapNhanVien) {
        // Lấy tài khoản của nhân viên
        NhanVien nhanVien = nhanVienRepository.findByTaiKhoan_TenDangNhap(tenDangNhapNhanVien)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));
        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();

        List<ChiTieu> chiTieuListToSave = new ArrayList<>();

        for (ChiTieuDTO dto : dtos) {
            // Bỏ qua nếu không có dữ liệu (dòng trống)
            if (dto.getNgayChi() == null && (dto.getTenKhoanChi() == null || dto.getTenKhoanChi().trim().isEmpty())) {
                continue;
            }

            // Validation
            if (dto.getNgayChi() == null || dto.getTenKhoanChi() == null || dto.getTenKhoanChi().trim().isEmpty() ||
                    dto.getSoTien() == null || dto.getSoTien().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Dữ liệu không hợp lệ: Ngày, Khoản chi, và Số tiền > 0 là bắt buộc.");
            }

            ChiTieu chiTieu;
            if (dto.getMaChiTieu() != null && !dto.getMaChiTieu().isEmpty()) {
                // Đây là SỬA
                chiTieu = chiTieuRepository.findById(dto.getMaChiTieu())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy chi tiêu: " + dto.getMaChiTieu()));
            } else {
                // Đây là THÊM MỚI
                chiTieu = new ChiTieu();
                chiTieu.setTaiKhoan(taiKhoan); // Chỉ gán tài khoản khi thêm mới
            }

            // Cập nhật dữ liệu
            chiTieu.setNgayChi(dto.getNgayChi());
            chiTieu.setTenKhoanChi(dto.getTenKhoanChi().trim());
            chiTieu.setSoTien(dto.getSoTien());

            chiTieuListToSave.add(chiTieu);
        }

        // Lưu tất cả thay đổi
        if (!chiTieuListToSave.isEmpty()) {
            chiTieuRepository.saveAll(chiTieuListToSave);
            System.out.println("Đã lưu " + chiTieuListToSave.size() + " khoản chi tiêu.");
        }
    }

    /**
     * PHƯƠG THỨC MỚI: Lấy các khoản chi gần đây (ví dụ: 7 ngày)
     * Được gọi bởi NganSachAdminController.showThemChiTieuForm
     */
    public List<ChiTieu> getRecentChiTieu() {
        // Lấy các khoản chi trong vòng 7 ngày qua
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return chiTieuRepository.findByNgayChiAfterOrderByNgayChiDesc(sevenDaysAgo);
    }
}