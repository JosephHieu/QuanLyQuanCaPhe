package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.ChucVu;
import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import com.josephhieu.quanlyquancaphe.entity.TaiKhoan;
import com.josephhieu.quanlyquancaphe.exception.UsernameAlreadyExistsException;
import com.josephhieu.quanlyquancaphe.repository.ChucVuRepository;
import com.josephhieu.quanlyquancaphe.repository.NhanVienRepository;
import com.josephhieu.quanlyquancaphe.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Lớp Service (Nghiệp vụ) cho các chức năng liên quan đến Nhân viên ({@link NhanVien}).
 * Bao gồm logic CRUD, xử lý Tài khoản liên kết, và Cập nhật Profile.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Service
public class NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private ChucVuRepository chucVuRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Lấy tất cả nhân viên (đã JOIN FETCH ChucVu để tối ưu).
     * @return Danh sách NhanVien.
     */
    public List<NhanVien> getAllNhanVien() {
        return nhanVienRepository.findAllWithChucVu();
    }

    /**
     * Tìm kiếm nhân viên theo Tên (keyword).
     * Nếu keyword rỗng, trả về tất cả nhân viên.
     *
     * @param keyword Từ khóa tìm kiếm.
     * @return Danh sách NhanVien khớp.
     */
    public List<NhanVien> searchNhanVienByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllNhanVien();
        }
        return nhanVienRepository.findByHoTenContainingIgnoreCase(keyword);
    }

    /**
     * Lấy NhanVien bằng ID (UUID).
     * @param maNhanVien Mã UUID của nhân viên.
     * @return Optional<NhanVien>.
     */
    public Optional<NhanVien> getNhanVienById(String maNhanVien) {
        return nhanVienRepository.findById(maNhanVien);
    }

    /**
     * Cập nhật thông tin nhân viên (do Admin thực hiện).
     * Cho phép đổi Chức vụ, Mật khẩu mới, và Ảnh.
     *
     * @param maNhanVien Mã UUID của nhân viên cần sửa.
     * @param dataFromForm Đối tượng NhanVien chứa (HoTen, DiaChi, SoDienThoai) mới.
     * @param maChucVuMoi Mã (UUID) của Chức vụ mới.
     * @param matKhauMoi Mật khẩu mới (thô), nếu rỗng thì không đổi.
     * @param anhFile File ảnh mới, nếu rỗng thì không đổi.
     * @throws NotFoundException Nếu không tìm thấy NhanVien hoặc ChucVu.
     * @throws IOException Nếu lỗi đọc file ảnh.
     */
    @Transactional
    public void updateNhanVien(
            String maNhanVien,
            NhanVien dataFromForm,
            String maChucVuMoi,
            String matKhauMoi,
            MultipartFile anhFile
    ) throws NotFoundException, IOException {

        // 1. Lấy nhân viên gốc
        NhanVien originalNhanVien = nhanVienRepository.findById(maNhanVien)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên với mã: " + maNhanVien));

        TaiKhoan taiKhoan = originalNhanVien.getTaiKhoan();
        if (taiKhoan == null) {
            throw new RuntimeException("Nhân viên này không có tài khoản.");
        }

        // 2. Cập nhật thông tin cơ bản
        originalNhanVien.setHoTen(dataFromForm.getHoTen());
        originalNhanVien.setDiaChi(dataFromForm.getDiaChi());
        originalNhanVien.setSoDienThoai(dataFromForm.getSoDienThoai());

        boolean taiKhoanUpdated = false; // Cờ theo dõi TaiKhoan có thay đổi không

        // 3. Cập nhật Chức vụ (nếu thay đổi)
        if (maChucVuMoi != null && !originalNhanVien.getChucVu().getMaChucVu().equals(maChucVuMoi)) {
            ChucVu chucVuMoi = chucVuRepository.findById(maChucVuMoi)
                    .orElseThrow(() -> new RuntimeException("Chức vụ mới không hợp lệ"));
            originalNhanVien.setChucVu(chucVuMoi);

            // Cập nhật QuyenHan trong TaiKhoan
            String quyenHanMoi = chucVuMoi.getTenChucVu().equalsIgnoreCase("Quản lý") ? "Admin" : "Staff";
            taiKhoan.setQuyenHan(quyenHanMoi);
            taiKhoanUpdated = true;
        }

        // 4. Cập nhật Mật khẩu (nếu có nhập)
        if (matKhauMoi != null && !matKhauMoi.trim().isEmpty()) {
            taiKhoan.setMatKhau(passwordEncoder.encode(matKhauMoi));
            taiKhoanUpdated = true;
        }

        // 5. Cập nhật Ảnh (nếu có chọn)
        if (anhFile != null && !anhFile.isEmpty()) {
            taiKhoan.setAnh(anhFile.getBytes());
            taiKhoanUpdated = true;
        }

        // 6. Lưu lại
        nhanVienRepository.save(originalNhanVien);
        if (taiKhoanUpdated) {
            taiKhoanRepository.save(taiKhoan);
        }
    }

    /**
     * Lấy NhanVien (bao gồm cả TaiKhoan) bằng Tên đăng nhập.
     * Dùng cho Trang cá nhân (Profile).
     *
     * @param tenDangNhap Tên đăng nhập.
     * @return Đối tượng NhanVien.
     * @throws RuntimeException Nếu không tìm thấy nhân viên.
     */
    public NhanVien getNhanVienByTenDangNhap(String tenDangNhap) {
        return nhanVienRepository.findByTaiKhoan_TenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với tên đăng nhập: " + tenDangNhap));
    }

    /**
     * Tạo một nhân viên mới và tài khoản liên quan.
     * (Logic này bị trùng với logic trong file bạn gửi,
     * tôi đã gộp lại và sửa lỗi kiểm tra `UsernameAlreadyExistsException`).
     *
     * @param nhanVien Đối tượng NhanVien (chứa HoTen, DiaChi, SoDienThoai).
     * @param tenDangNhap Tên đăng nhập cho tài khoản mới.
     * @param matKhau Mật khẩu thô (chưa mã hóa).
     * @param maChucVu Mã UUID của ChucVu.
     * @param anhFile File ảnh đại diện.
     * @return Đối tượng NhanVien đã được lưu.
     * @throws UsernameAlreadyExistsException Nếu tên đăng nhập đã tồn tại.
     * @throws NotFoundException Nếu maChucVu không hợp lệ.
     * @throws IOException Nếu có lỗi khi đọc file ảnh.
     */
    @Transactional
    public NhanVien createNhanVien(
            NhanVien nhanVien,
            String tenDangNhap,
            String matKhau,
            String maChucVu,
            MultipartFile anhFile
    ) throws UsernameAlreadyExistsException, IOException, NotFoundException {

        // 1. Kiểm tra trùng tên đăng nhập
        if (taiKhoanRepository.findByTenDangNhap(tenDangNhap).isPresent()) {
            throw new UsernameAlreadyExistsException("Tên đăng nhập " + tenDangNhap + " đã tồn tại!");
        }

        // 2. Tìm Chức vụ
        ChucVu chucVu = chucVuRepository.findById(maChucVu)
                .orElseThrow(() -> new RuntimeException("Chức vụ không hợp lệ"));

        // 3. Tạo tài khoản
        TaiKhoan newTaiKhoan = new TaiKhoan();
        newTaiKhoan.setTenDangNhap(tenDangNhap);
        newTaiKhoan.setMatKhau(passwordEncoder.encode(matKhau));
        String quyenHan = chucVu.getTenChucVu().equalsIgnoreCase("Quản lý") ? "Admin" : "Staff";
        newTaiKhoan.setQuyenHan(quyenHan);
        if (anhFile != null && !anhFile.isEmpty()) {
            newTaiKhoan.setAnh(anhFile.getBytes());
        }
        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(newTaiKhoan);

        // 4. Gán tài khoản, chức vụ và lưu nhân viên
        nhanVien.setTaiKhoan(savedTaiKhoan);
        nhanVien.setChucVu(chucVu);
        return nhanVienRepository.save(nhanVien);
    }

    /**
     * Cập nhật thông tin profile cá nhân (do người dùng tự thực hiện).
     * Chỉ cho phép đổi thông tin cơ bản và ảnh, KHÔNG cho đổi Chức vụ, Lương, Mật khẩu.
     *
     * @param tenDangNhap Tên đăng nhập của người dùng (để xác thực).
     * @param dataFromForm Đối tượng NhanVien chứa (HoTen, DiaChi, SoDienThoai) mới.
     * @param anhFile File ảnh mới (nếu có).
     * @throws IOException Nếu lỗi đọc file ảnh.
     * @throws RuntimeException Nếu không tìm thấy nhân viên/tài khoản.
     */
    @Transactional
    public void updateNhanVienProfile(
            String tenDangNhap,
            NhanVien dataFromForm,
            MultipartFile anhFile
    ) throws IOException {

        // 1. Lấy NhanVien gốc
        NhanVien originalNhanVien = this.getNhanVienByTenDangNhap(tenDangNhap);
        if (originalNhanVien.getTaiKhoan() == null) {
            throw new RuntimeException("Nhân viên này không có thông tin tài khoản.");
        }
        TaiKhoan taiKhoan = originalNhanVien.getTaiKhoan();

        // 2. Cập nhật thông tin NhanVien
        originalNhanVien.setHoTen(dataFromForm.getHoTen());
        originalNhanVien.setDiaChi(dataFromForm.getDiaChi());
        originalNhanVien.setSoDienThoai(dataFromForm.getSoDienThoai());

        // 3. Cập nhật Ảnh (nếu có)
        boolean taiKhoanUpdated = false;
        if (anhFile != null && !anhFile.isEmpty()) {
            taiKhoan.setAnh(anhFile.getBytes());
            taiKhoanUpdated = true;
        }

        // 4. Lưu lại
        nhanVienRepository.save(originalNhanVien);
        if (taiKhoanUpdated) {
            taiKhoanRepository.save(taiKhoan);
        }
    }

    /**
     * Xóa một Nhân viên VÀ Tài khoản liên quan.
     *
     * @param maNhanVien Mã (UUID) của nhân viên cần xóa.
     * @throws NotFoundException Nếu không tìm thấy nhân viên.
     * @throws DataIntegrityViolationException Nếu nhân viên đang có liên kết
     * (ví dụ: đã tạo Đơn xuất, Hóa đơn).
     */
    @Transactional
    public void deleteNhanVien(String maNhanVien) throws NotFoundException, DataIntegrityViolationException {

        // 1. Tìm nhân viên
        NhanVien nhanVien = nhanVienRepository.findById(maNhanVien)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên để xóa: " + maNhanVien));

        // 2. Lấy tài khoản liên quan
        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();

        try {
            // 3. Xóa NhanVien trước
            // (CSDL sẽ ném lỗi nếu NhanVien đang là khóa ngoại ở DonNhap, DonXuat, ChiTietDatBan)
            nhanVienRepository.delete(nhanVien);
            System.out.println("Đã xóa nhân viên ID: " + maNhanVien);

            // 4. Nếu xóa NhanVien thành công, xóa TaiKhoan
            if (taiKhoan != null) {
                // (Cần đảm bảo TaiKhoan không còn là khóa ngoại ở ChiTieu)
                // (Nếu logic của bạn yêu cầu xóa ChiTieu trước, bạn cần thêm code ở đây)
                taiKhoanRepository.delete(taiKhoan);
                System.out.println("Đã xóa tài khoản liên quan ID: " + taiKhoan.getMaTaiKhoan());
            }

        } catch (DataIntegrityViolationException e) {
            // 5. Bắt lỗi ràng buộc khóa ngoại
            System.err.println("Lỗi xóa nhân viên do ràng buộc: " + e.getMessage());
            throw new DataIntegrityViolationException("Không thể xóa nhân viên này vì đang có liên kết (ví dụ: đã lập hóa đơn, đơn xuất/nhập, hoặc còn khoản chi).");
        }
    }
}