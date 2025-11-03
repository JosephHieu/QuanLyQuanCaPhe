package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.dto.NguyenLieuDropdownDTO;
import com.josephhieu.quanlyquancaphe.entity.*;
import com.josephhieu.quanlyquancaphe.entity.id.DonNhapId;
import com.josephhieu.quanlyquancaphe.exception.InsufficientStockException;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Lớp Service (Nghiệp vụ) cho các chức năng liên quan đến
 * Quản lý Kho hàng ({@link HangHoa}).
 * Bao gồm logic Nhập, Xuất, Sửa, Xóa, Tìm kiếm, và Lấy danh sách hàng hóa.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Service
public class HangHoaService {

    @Autowired
    private HangHoaRepository hangHoaRepository;

    @Autowired
    private DonViTinhRepository donViTinhRepository;

    @Autowired
    private DonNhapRepository donNhapRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private DonXuatRepository donXuatRepository;

    /**
     * Lấy tất cả hàng hóa (nguyên vật liệu) trong kho,
     * sắp xếp theo Tên hàng hóa (A-Z).
     *
     * @return Danh sách (List) các đối tượng {@link HangHoa}.
     */
    public List<HangHoa> getAllHangHoa() {
        return hangHoaRepository.findAllByOrderByTenHangHoaAsc();
    }

    /**
     * Xử lý nghiệp vụ Nhập hàng hóa.
     * 1. Tìm hoặc tạo mới {@link HangHoa} dựa trên tên.
     * 2. Cập nhật (cộng dồn) số lượng tồn kho.
     * 3. (Đang tạm dừng) Ghi lại giao dịch vào bảng {@link DonNhap}.
     *
     * @param tenHangHoa     Tên hàng hóa (từ form).
     * @param soLuongNhap    Số lượng nhập.
     * @param maDonViTinh    Mã (UUID) của Đơn vị tính.
     * @param donGiaNhap     Đơn giá tại thời điểm nhập.
     * @param ngayNhap       Ngày nhập hàng.
     * @param tenDangNhapNhanVien Tên đăng nhập của nhân viên thực hiện.
     * @throws NotFoundException Nếu `maDonViTinh` hoặc `tenDangNhapNhanVien` không hợp lệ.
     */
    @Transactional
    public void nhapHangHoa(String tenHangHoa, int soLuongNhap, String maDonViTinh, BigDecimal donGiaNhap, LocalDate ngayNhap, String tenDangNhapNhanVien) {

        if (soLuongNhap <= 0) {
            throw new IllegalArgumentException("Số lượng nhập phải lớn hơn 0.");
        }

        // 1. Tìm đơn vị tính
        DonViTinh donViTinh = donViTinhRepository.findById(maDonViTinh)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn vị tính: " + maDonViTinh));

        // 2. Tìm nhân viên thực hiện
        NhanVien nhanVien = nhanVienRepository.findByTaiKhoan_TenDangNhap(tenDangNhapNhanVien)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên thực hiện."));

        // 3. Tìm hàng hóa theo tên (không phân biệt hoa/thường)
        Optional<HangHoa> existingHangHoaOpt = hangHoaRepository.findByTenHangHoaIgnoreCase(tenHangHoa.trim());

        HangHoa hangHoa;
        if (existingHangHoaOpt.isPresent()) {
            // Hàng hóa đã tồn tại -> Cập nhật số lượng
            hangHoa = existingHangHoaOpt.get();
            hangHoa.setSoLuong(hangHoa.getSoLuong() + soLuongNhap);
            hangHoa.setDonGia(donGiaNhap); // Cập nhật đơn giá mới nhất
            hangHoa.setDonViTinh(donViTinh); // Cập nhật đơn vị tính (nếu thay đổi)
            System.out.println("Cập nhật số lượng cho hàng hóa: " + tenHangHoa);
        } else {
            // Hàng hóa mới -> Tạo mới
            hangHoa = new HangHoa();
            hangHoa.setTenHangHoa(tenHangHoa.trim());
            hangHoa.setSoLuong(soLuongNhap);
            hangHoa.setDonViTinh(donViTinh);
            hangHoa.setDonGia(donGiaNhap);
            System.out.println("Tạo mới hàng hóa: " + tenHangHoa);
        }
        HangHoa savedHangHoa = hangHoaRepository.save(hangHoa); // Lưu (Thêm/Sửa) hàng hóa

        // 4. Ghi lại giao dịch vào bảng DonNhap
        // !!! CẢNH BÁO: Logic này đang bị tạm dừng (comment)
        // Cần xem lại thiết kế bảng DonNhap, đặc biệt là vai trò của MaThietBi
        // khi nhập hàng hóa.
        /*
        DonNhap donNhap = new DonNhap();
        DonNhapId donNhapId = new DonNhapId();
        donNhapId.setMaNhanVien(nhanVien.getMaNhanVien());
        donNhapId.setMaHangHoa(savedHangHoa.getMaHangHoa());
        // donNhapId.setMaThietBi("..."); // MaThietBi đang là bắt buộc (NOT NULL)
        donNhap.setId(donNhapId);
        donNhap.setNhanVien(nhanVien);
        donNhap.setHangHoa(savedHangHoa);
        // donNhap.setThietBi(...); // Cần đối tượng ThietBi
        donNhap.setNgayNhap(ngayNhap);
        donNhap.setSoLuong(soLuongNhap);
        donNhap.setTongTien(donGiaNhap.multiply(BigDecimal.valueOf(soLuongNhap)));

        donNhapRepository.save(donNhap); // Lưu phiếu nhập
        System.out.println("Đã ghi lại phiếu nhập kho.");
        */
    }

    /**
     * Xử lý nghiệp vụ Xuất hàng hóa.
     * 1. Kiểm tra tồn kho.
     * 2. Trừ lùi số lượng tồn kho.
     * 3. Ghi lại giao dịch vào bảng {@link DonXuat}.
     *
     * @param maHangHoa     Mã (UUID) của hàng hóa cần xuất.
     * @param soLuongXuat   Số lượng xuất.
     * @param ngayXuat      Ngày xuất.
     * @param tenDangNhapNhanVien Tên đăng nhập của nhân viên thực hiện.
     * @throws NotFoundException Nếu `maHangHoa` không tồn tại.
     * @throws InsufficientStockException Nếu số lượng xuất > số lượng tồn kho.
     */
    @Transactional
    public void xuatHangHoa(String maHangHoa, int soLuongXuat, LocalDate ngayXuat, String tenDangNhapNhanVien)
            throws NotFoundException, InsufficientStockException {

        if (soLuongXuat <= 0) {
            throw new IllegalArgumentException("Số lượng xuất phải lớn hơn 0.");
        }

        // 1. Tìm hàng hóa cần xuất
        HangHoa hangHoa = hangHoaRepository.findById(maHangHoa)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa với mã: " + maHangHoa));

        // 2. Kiểm tra tồn kho
        if (hangHoa.getSoLuong() < soLuongXuat) {
            throw new InsufficientStockException("Không đủ số lượng tồn kho cho '" + hangHoa.getTenHangHoa() +
                    "'. Hiện có: " + hangHoa.getSoLuong() + ", cần xuất: " + soLuongXuat);
        }

        // 3. Tìm nhân viên thực hiện
        NhanVien nhanVien = nhanVienRepository.findByTaiKhoan_TenDangNhap(tenDangNhapNhanVien)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên thực hiện."));

        // 4. Trừ kho (Cập nhật số lượng)
        hangHoa.setSoLuong(hangHoa.getSoLuong() - soLuongXuat);
        hangHoaRepository.save(hangHoa);
        System.out.println("Đã cập nhật số lượng tồn kho cho: " + hangHoa.getTenHangHoa());

        // 5. Ghi lại giao dịch vào bảng DonXuat
        DonXuat donXuat = new DonXuat();
        // MaDonXuat sẽ được tự tạo bằng @GeneratedValue(strategy = GenerationType.UUID)
        donXuat.setNhanVien(nhanVien);
        donXuat.setHangHoa(hangHoa);
        donXuat.setNgayXuat(ngayXuat);
        donXuat.setSoLuong(soLuongXuat);
        // Tính TongTienXuat dựa trên đơn giá hiện tại của hàng hóa
        donXuat.setTongTienXuat(hangHoa.getDonGia().multiply(BigDecimal.valueOf(soLuongXuat)));

        donXuatRepository.save(donXuat); // Lưu phiếu xuất
        System.out.println("Đã ghi lại phiếu xuất kho.");
    }

    /**
     * Lấy thông tin một Hàng hóa bằng ID (UUID).
     *
     * @param maHangHoa Mã (UUID) của hàng hóa.
     * @return Optional<HangHoa> chứa hàng hóa nếu tìm thấy.
     */
    public Optional<HangHoa> getHangHoaById(String maHangHoa) {
        return hangHoaRepository.findById(maHangHoa);
    }

    /**
     * Phương thức lưu (Thêm mới hoặc Cập nhật) Hàng hóa cơ bản.
     * (Chủ yếu được gọi bởi `updateHangHoa`).
     *
     * @param hangHoa Đối tượng HangHoa cần lưu.
     * @return Đối tượng HangHoa đã được lưu.
     * @throws IllegalArgumentException Nếu thông tin không hợp lệ.
     */
    @Transactional
    public HangHoa saveHangHoa(HangHoa hangHoa) {
        // Validation cơ bản
        if (hangHoa.getTenHangHoa() == null || hangHoa.getTenHangHoa().trim().isEmpty() ||
                hangHoa.getDonViTinh() == null || hangHoa.getDonViTinh().getMaDonViTinh() == null ||
                hangHoa.getSoLuong() < 0 || hangHoa.getDonGia() == null || hangHoa.getDonGia().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Thông tin hàng hóa không hợp lệ.");
        }
        return hangHoaRepository.save(hangHoa);
    }

    /**
     * Cập nhật thông tin chi tiết của một Hàng hóa (dùng cho form Sửa).
     *
     * @param maHangHoa     Mã (UUID) của hàng hóa cần sửa.
     * @param dataFromForm  Đối tượng HangHoa chứa dữ liệu mới từ form (Tên, SL, Đơn giá).
     * @param maDonViTinh   Mã (UUID) của Đơn vị tính mới.
     * @return Đối tượng HangHoa đã được cập nhật.
     * @throws NotFoundException Nếu `maHangHoa` hoặc `maDonViTinh` không tồn tại.
     */
    @Transactional
    public HangHoa updateHangHoa(String maHangHoa, HangHoa dataFromForm, String maDonViTinh) throws NotFoundException {
        // 1. Lấy HangHoa gốc từ CSDL
        HangHoa originalHangHoa = hangHoaRepository.findById(maHangHoa)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa: " + maHangHoa));

        // 2. Tìm DonViTinh mới
        DonViTinh donViTinh = donViTinhRepository.findById(maDonViTinh)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn vị tính: " + maDonViTinh));

        // 3. Cập nhật các trường
        originalHangHoa.setTenHangHoa(dataFromForm.getTenHangHoa().trim());
        originalHangHoa.setSoLuong(dataFromForm.getSoLuong()); // Cảnh báo: Sửa trực tiếp tồn kho
        originalHangHoa.setDonGia(dataFromForm.getDonGia());
        originalHangHoa.setDonViTinh(donViTinh);

        // 4. Lưu lại
        return hangHoaRepository.save(originalHangHoa);
    }

    /**
     * Tìm kiếm hàng hóa dựa trên từ khóa (keyword).
     * Nếu từ khóa rỗng, trả về tất cả hàng hóa (theo yêu cầu nghiệp vụ).
     *
     * @param keyword Từ khóa tìm kiếm.
     * @return Danh sách HangHoa khớp.
     */
    public List<HangHoa> searchHangHoa(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllHangHoa(); // Trả về tất cả nếu không có từ khóa
        }
        return hangHoaRepository.findByTenHangHoaContainingIgnoreCase(keyword.trim());
    }

    /**
     * Xóa một Hàng hóa dựa trên ID (UUID).
     *
     * @param maHangHoa Mã (UUID) của hàng hóa cần xóa.
     * @throws NotFoundException Nếu không tìm thấy hàng hóa.
     * @throws DataIntegrityViolationException Nếu hàng hóa đang được sử dụng ở bảng khác
     * (DonNhap, DonXuat, ChiTietThucDon).
     */
    @Transactional
    public void deleteHangHoa(String maHangHoa) throws NotFoundException, DataIntegrityViolationException {
        // 1. Kiểm tra tồn tại
        if (!hangHoaRepository.existsById(maHangHoa)) {
            throw new NotFoundException("Không tìm thấy hàng hóa để xóa: " + maHangHoa);
        }
        // 2. Thử xóa
        try {
            hangHoaRepository.deleteById(maHangHoa);
            System.out.println("Đã xóa hàng hóa ID: " + maHangHoa);
        } catch (DataIntegrityViolationException e) {
            // 3. Bắt lỗi nếu CSDL không cho phép xóa
            System.err.println("Lỗi xóa hàng hóa do ràng buộc: " + e.getMessage());
            throw new DataIntegrityViolationException("Không thể xóa hàng hóa này vì đang được sử dụng (ví dụ: đã nhập/xuất kho hoặc có trong thực đơn).");
        }
    }

    /**
     * Lấy danh sách nguyên liệu (Hàng hóa) dưới dạng DTO
     * để sử dụng trong dropdown (ô chọn) ở frontend.
     * Phương thức này sử dụng query JOIN FETCH để giải quyết lỗi N+1
     * và lỗi Lazy Loading khi chuyển đổi sang JSON.
     *
     * @return Danh sách {@link NguyenLieuDropdownDTO}.
     */
    @Transactional(readOnly = true)
    public List<NguyenLieuDropdownDTO> getNguyenLieuForDropdown() {
        // Gọi query JOIN FETCH (findAllWithDonViTinh)
        return hangHoaRepository.findAllWithDonViTinh().stream()
                .map(hh -> new NguyenLieuDropdownDTO(
                        hh.getMaHangHoa(),
                        hh.getTenHangHoa(),
                        (hh.getDonViTinh() != null) ? hh.getDonViTinh().getTenDonVi() : "" // Lấy Tên ĐVT (an toàn)
                ))
                .collect(Collectors.toList());
    }
}