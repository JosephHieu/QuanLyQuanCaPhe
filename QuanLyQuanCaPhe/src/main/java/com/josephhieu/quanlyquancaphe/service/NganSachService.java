package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.dto.ChiTieuDTO;
import com.josephhieu.quanlyquancaphe.dto.ThuChiNgayDTO;
import com.josephhieu.quanlyquancaphe.dto.TongThuChiDTO;
import com.josephhieu.quanlyquancaphe.entity.ChiTieu;
import com.josephhieu.quanlyquancaphe.entity.HoaDon;
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
import java.util.stream.Collectors;

/**
 * Lớp Service (Nghiệp vụ) cho các chức năng liên quan đến Ngân sách.
 * Bao gồm logic tổng hợp Thu/Chi cho báo cáo và CRUD các khoản Chi tiêu.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Service
public class NganSachService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTieuRepository chiTieuRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    /**
     * Tổng hợp dữ liệu Thu (từ Hóa đơn đã thanh toán) và Chi (từ Chi tiêu)
     * trong một khoảng ngày và nhóm theo ngày.
     * Sử dụng Java Streams để xử lý việc nhóm và tính tổng (thay vì JPQL phức tạp).
     *
     * @param startDate Ngày bắt đầu.
     * @param endDate   Ngày kết thúc.
     * @return Một {@link TongThuChiDTO} chứa danh sách chi tiết và tổng cộng.
     */
    @Transactional(readOnly = true)
    public TongThuChiDTO getTongHopThuChi(LocalDate startDate, LocalDate endDate) {

        // 1. Lấy danh sách thô Hóa đơn (Thu) đã thanh toán
        List<HoaDon> danhSachThu = hoaDonRepository.findByTrangThaiTrueAndNgayGioTaoBetween(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay() // .plusDays(1) để bao gồm cả ngày kết thúc
        );

        // 2. Lấy danh sách thô Chi tiêu (Chi)
        List<ChiTieu> danhSachChi = chiTieuRepository.findByNgayChiBetween(startDate, endDate);

        // 3. Dùng Streams: Nhóm Hóa đơn theo Ngày và tính tổng TongTien
        Map<LocalDate, BigDecimal> mapThu = danhSachThu.stream()
                .collect(Collectors.groupingBy(
                        hd -> hd.getNgayGioTao().toLocalDate(), // Key là Ngày
                        TreeMap::new, // Dùng TreeMap để tự sắp xếp theo Ngày
                        Collectors.reducing(BigDecimal.ZERO, HoaDon::getTongTien, BigDecimal::add) // Tính tổng
                ));

        // 4. Dùng Streams: Nhóm Chi tiêu theo Ngày và tính tổng SoTien
        Map<LocalDate, BigDecimal> mapChi = danhSachChi.stream()
                .collect(Collectors.groupingBy(
                        ChiTieu::getNgayChi, // Key là Ngày
                        TreeMap::new, // Dùng TreeMap
                        Collectors.reducing(BigDecimal.ZERO, ChiTieu::getSoTien, BigDecimal::add) // Tính tổng
                ));

        // 5. Gộp 2 Map (Thu và Chi) lại
        Map<LocalDate, ThuChiNgayDTO> mapTongHop = new TreeMap<>();
        mapThu.forEach((ngay, tongThu) -> {
            mapTongHop.put(ngay, new ThuChiNgayDTO(ngay, tongThu, BigDecimal.ZERO)); // Thêm Thu
        });
        mapChi.forEach((ngay, tongChi) -> {
            mapTongHop.compute(ngay, (key, dto) -> {
                if (dto == null) {
                    return new ThuChiNgayDTO(key, BigDecimal.ZERO, tongChi); // Thêm Chi (nếu ngày chưa có)
                } else {
                    dto.setTongChi(tongChi); // Cập nhật Chi (nếu ngày đã có từ Thu)
                    return dto;
                }
            });
        });

        // 6. Tính toán tổng cộng
        BigDecimal tongThuCong = mapThu.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tongChiCong = mapChi.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        // 7. Trả về DTO tổng hợp
        return new TongThuChiDTO(
                new ArrayList<>(mapTongHop.values()), // Chuyển Map về List
                tongThuCong,
                tongChiCong
        );
    }

    /**
     * Lưu (Thêm mới hoặc Cập nhật) một danh sách các khoản Chi tiêu
     * từ form động "Thêm chi tiêu".
     *
     * @param dtos                Danh sách {@link ChiTieuDTO} từ form.
     * @param tenDangNhapNhanVien Tên đăng nhập của người thực hiện.
     * @throws NotFoundException Nếu không tìm thấy nhân viên hoặc khoản chi cần sửa.
     * @throws IllegalArgumentException Nếu dữ liệu DTO không hợp lệ.
     */
    @Transactional
    public void saveChiTieuList(List<ChiTieuDTO> dtos, String tenDangNhapNhanVien) {
        // Lấy tài khoản của nhân viên
        NhanVien nhanVien = nhanVienRepository.findByTaiKhoan_TenDangNhap(tenDangNhapNhanVien)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));
        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();

        List<ChiTieu> chiTieuListToSave = new ArrayList<>();

        for (ChiTieuDTO dto : dtos) {
            // Bỏ qua nếu dòng trống (không có ngày VÀ không có tên)
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
                // SỬA: Tìm khoản chi cũ
                chiTieu = chiTieuRepository.findById(dto.getMaChiTieu())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy chi tiêu: " + dto.getMaChiTieu()));
            } else {
                // THÊM MỚI: Tạo khoản chi mới
                chiTieu = new ChiTieu();
                chiTieu.setTaiKhoan(taiKhoan); // Gán tài khoản (người chi)
            }

            // Cập nhật dữ liệu
            chiTieu.setNgayChi(dto.getNgayChi());
            chiTieu.setTenKhoanChi(dto.getTenKhoanChi().trim());
            chiTieu.setSoTien(dto.getSoTien());

            chiTieuListToSave.add(chiTieu);
        }

        // Lưu tất cả thay đổi (Thêm mới và Sửa) vào CSDL
        if (!chiTieuListToSave.isEmpty()) {
            chiTieuRepository.saveAll(chiTieuListToSave);
            System.out.println("Đã lưu " + chiTieuListToSave.size() + " khoản chi tiêu.");
        }
    }

    /**
     * Lấy các khoản chi tiêu gần đây (ví dụ: trong vòng 7 ngày qua).
     * Dùng để hiển thị sẵn trong form "Thêm chi tiêu".
     *
     * @return Danh sách {@link ChiTieu}.
     */
    public List<ChiTieu> getRecentChiTieu() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return chiTieuRepository.findByNgayChiAfterOrderByNgayChiDesc(sevenDaysAgo);
    }
}