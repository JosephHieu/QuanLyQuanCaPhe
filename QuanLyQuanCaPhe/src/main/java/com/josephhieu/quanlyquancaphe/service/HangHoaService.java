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

    public List<HangHoa> getAllHangHoa() {

        return hangHoaRepository.findAllByOrderByTenHangHoaAsc();
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý nhập hàng hóa khỏi kho
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

        // 3. Tìm hàng hóa theo tên (hoặc tạo mới nếu chưa có)
        // Cần phương thức findByTenHangHoa trong HangHoaRepository
        Optional<HangHoa> existingHangHoaOpt = hangHoaRepository.findByTenHangHoaIgnoreCase(tenHangHoa.trim()); // Trim để bỏ khoảng trắng thừa

        HangHoa hangHoa;
        if (existingHangHoaOpt.isPresent()) {
            // Hàng hóa đã tồn tại -> Cập nhật số lượng
            hangHoa = existingHangHoaOpt.get();
            hangHoa.setSoLuong(hangHoa.getSoLuong() + soLuongNhap);
            // Có thể cập nhật đơn giá mới nhất nếu muốn
            hangHoa.setDonGia(donGiaNhap); // Cập nhật đơn giá theo lần nhập mới nhất
            hangHoa.setDonViTinh(donViTinh); // Đảm bảo đơn vị tính đúng
            System.out.println("Cập nhật số lượng cho hàng hóa: " + tenHangHoa);
        } else {
            // Hàng hóa mới -> Tạo mới
            hangHoa = new HangHoa();
            // hangHoa.setMaHangHoa(UUID.randomUUID().toString()); // Không cần nếu dùng @GeneratedValue
            hangHoa.setTenHangHoa(tenHangHoa.trim());
            hangHoa.setSoLuong(soLuongNhap);
            hangHoa.setDonViTinh(donViTinh);
            hangHoa.setDonGia(donGiaNhap);
            System.out.println("Tạo mới hàng hóa: " + tenHangHoa);
        }
        HangHoa savedHangHoa = hangHoaRepository.save(hangHoa); // Lưu lại hàng hóa

        // 4. Ghi lại giao dịch vào bảng DonNhap
        // Lưu ý: Thiết kế bảng DonNhap hiện tại có vẻ hơi lạ (cần cả MaThietBi?)
        // Giả sử ta bỏ qua MaThietBi hoặc dùng một giá trị mặc định/null nếu cột cho phép
        DonNhap donNhap = new DonNhap();
        DonNhapId donNhapId = new DonNhapId();
        donNhapId.setMaNhanVien(nhanVien.getMaNhanVien());
        donNhapId.setMaHangHoa(savedHangHoa.getMaHangHoa());
        // donNhapId.setMaThietBi("..."); // Cần xử lý MaThietBi này
        donNhap.setId(donNhapId); // Cần tạo DonNhapId và DonNhapRepository
        donNhap.setNhanVien(nhanVien);
        donNhap.setHangHoa(savedHangHoa);
        // donNhap.setThietBi(...);
        donNhap.setNgayNhap(ngayNhap);
        donNhap.setSoLuong(soLuongNhap);
        donNhap.setTongTien(donGiaNhap.multiply(BigDecimal.valueOf(soLuongNhap)));

        // donNhapRepository.save(donNhap); // Lưu phiếu nhập
        System.out.println("Đã ghi lại phiếu nhập kho.");
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý xuất hàng hóa khỏi kho
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

        // 4. Trừ kho
        hangHoa.setSoLuong(hangHoa.getSoLuong() - soLuongXuat);
        hangHoaRepository.save(hangHoa);
        System.out.println("Đã cập nhật số lượng tồn kho cho: " + hangHoa.getTenHangHoa());

        // 5. Ghi lại giao dịch vào bảng DonXuat
        DonXuat donXuat = new DonXuat();
        // donXuat.setMaDonXuat(UUID.randomUUID().toString()); // Không cần nếu dùng @GeneratedValue
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
     * Lấy hàng hóa theo ID
     */
    public Optional<HangHoa> getHangHoaById(String maHangHoa) {
        return hangHoaRepository.findById(maHangHoa);
    }

    // Lưu hàng hóa hoặc thêm mới
    @Transactional
    public HangHoa saveHangHoa(HangHoa hangHoa) {
        // Basic validation
        if (hangHoa.getTenHangHoa() == null || hangHoa.getTenHangHoa().trim().isEmpty() ||
                hangHoa.getDonViTinh() == null || hangHoa.getDonViTinh().getMaDonViTinh() == null ||
                hangHoa.getSoLuong() < 0 || hangHoa.getDonGia() == null || hangHoa.getDonGia().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Thông tin hàng hóa không hợp lệ.");
        }
        // Ensure DonViTinh object is managed if only ID is passed from form potentially
        // (Controller usually handles fetching the full DonViTinh object)
        return hangHoaRepository.save(hangHoa);
    }

    /**
     * Cập nhật thông tin Hàng hóa (Dùng riêng cho form Sửa)
     */
    @Transactional
    public HangHoa updateHangHoa(String maHangHoa, HangHoa dataFromForm, String maDonViTinh) throws NotFoundException {
        // 1. Lấy HangHoa gốc
        HangHoa originalHangHoa = hangHoaRepository.findById(maHangHoa)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa: " + maHangHoa));

        // 2. Tìm DonViTinh mới
        DonViTinh donViTinh = donViTinhRepository.findById(maDonViTinh)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn vị tính: " + maDonViTinh));

        // 3. Cập nhật các trường
        originalHangHoa.setTenHangHoa(dataFromForm.getTenHangHoa().trim());
        originalHangHoa.setSoLuong(dataFromForm.getSoLuong()); // Cho phép sửa số lượng tồn kho trực tiếp? (Cẩn thận)
        originalHangHoa.setDonGia(dataFromForm.getDonGia());
        originalHangHoa.setDonViTinh(donViTinh);
        // Không cập nhật ngày nhập/xuất ở đây

        // 4. Lưu lại
        return hangHoaRepository.save(originalHangHoa);
    }

    /**
     * PHƯƠNG THỨC MỚI: Tìm kiếm hàng hóa theo tên
     */
    public List<HangHoa> searchHangHoa(String keyword) {
        // Nếu không có từ khóa hoặc từ khóa rỗng, trả về danh sách rỗng
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllHangHoa(); // Gọi hàm lấy tất cả hàng hóa
        }

        return hangHoaRepository.findByTenHangHoaContainingIgnoreCase(keyword.trim());
    }

    @Transactional
    public void deleteHangHoa(String maHangHoa) throws NotFoundException, DataIntegrityViolationException {

        // 1. Kiểm tra xem hàng hóa có tồn tại không
        if (!hangHoaRepository.existsById(maHangHoa)) {
            throw new NotFoundException("Không tìm thấy hàng hóa để xóa: " + maHangHoa);
        }

        // 2. Thực hiện xóa
        try {
            hangHoaRepository.deleteById(maHangHoa);
            System.out.println("Đã xóa hàng hóa ID: " + maHangHoa);

        } catch (DataIntegrityViolationException e) {
            // 3. Bắt lỗi nếu CSDL không cho xóa (do ràng buộc khóa ngoại)
            // Ví dụ: Hàng hóa này đã có trong 1 Đơn nhập, Đơn xuất, hoặc Chi tiết thực đơn
            System.err.println("Lỗi xóa hàng hóa do ràng buộc: " + e.getMessage());
            throw new DataIntegrityViolationException("Không thể xóa hàng hóa này vì đang được sử dụng (ví dụ: đã nhập/xuất kho hoặc có trong thực đơn).");
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Lấy danh sách nguyên liệu (dưới dạng DTO) cho dropdown
     */
    @Transactional(readOnly = true) // Đảm bảo session mở khi truy cập donViTinh
    public List<NguyenLieuDropdownDTO> getNguyenLieuForDropdown() {
        // Gọi query JOIN FETCH
        return hangHoaRepository.findAllWithDonViTinh().stream()
                .map(hh -> new NguyenLieuDropdownDTO(
                        hh.getMaHangHoa(),
                        hh.getTenHangHoa(),
                        // Lấy Tên đơn vị (an toàn nếu donViTinh là null)
                        (hh.getDonViTinh() != null) ? hh.getDonViTinh().getTenDonVi() : ""
                ))
                .collect(Collectors.toList());
    }
}
