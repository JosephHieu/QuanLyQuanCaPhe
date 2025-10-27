package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.ChucVu;
import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import com.josephhieu.quanlyquancaphe.entity.TaiKhoan;
import com.josephhieu.quanlyquancaphe.exception.UsernameAlreadyExistsException;
import com.josephhieu.quanlyquancaphe.repository.ChucVuRepository;
import com.josephhieu.quanlyquancaphe.repository.NhanVienRepository;

import com.josephhieu.quanlyquancaphe.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    // Lấy tất cả nhân viên
    public List<NhanVien> getAllNhanVien() {
        return nhanVienRepository.findAll();
    }

    public NhanVien getNhanVienByTenDangNhap(String tenDangNhap) {

        return nhanVienRepository.findByTaiKhoan_TenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với tên đăng nhập: " + tenDangNhap));
    }

    @Transactional
    public NhanVien createNhanVien(
            NhanVien nhanVien,
            String tenDangNhap,
            String matKhau,
            String maChucVu,
            MultipartFile anhFile // <-- THÊM THAM SỐ FILE
    ) throws UsernameAlreadyExistsException, IOException { // <-- Thêm IOException

        System.out.println("--- SERVICE searching for maChucVu: [" + maChucVu + "]");
        Optional<ChucVu> optionalChucVu = chucVuRepository.findById(maChucVu);
        System.out.println("--- SERVICE found ChucVu? " + optionalChucVu.isPresent());

        ChucVu chucVu = chucVuRepository.findById(maChucVu)
                .orElseThrow(() -> new RuntimeException("Chức vụ không hợp lệ"));

        // Tạo tài khoản
        TaiKhoan newTaiKhoan = new TaiKhoan();
        newTaiKhoan.setTenDangNhap(tenDangNhap);
        newTaiKhoan.setMatKhau(passwordEncoder.encode(matKhau));
        // ... (Set QuyenHan) ...
        // Xác định quyền hạn dựa trên ChucVu
        String quyenHan = chucVu.getTenChucVu().equalsIgnoreCase("Quản lý") ? "Admin" : "Staff";
        newTaiKhoan.setQuyenHan(quyenHan);

        // *** XỬ LÝ FILE ẢNH ***
        if (anhFile != null && !anhFile.isEmpty()) {
            newTaiKhoan.setAnh(anhFile.getBytes()); // Đọc dữ liệu file và lưu vào byte[]
        }
        // *** KẾT THÚC XỬ LÝ ẢNH ***

        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(newTaiKhoan);

        // ... (Gán tài khoản, chức vụ và lưu nhân viên) ...
        nhanVien.setTaiKhoan(savedTaiKhoan);
        nhanVien.setChucVu(chucVu);

        return nhanVienRepository.save(nhanVien);
    }

    @Transactional // Đảm bảo các thay đổi được lưu cùng lúc
    public void updateNhanVienProfile(
            String tenDangNhap,
            NhanVien dataFromForm,
            MultipartFile anhFile
    ) throws IOException { // Khai báo có thể ném IOException

        // 1. Lấy NhanVien gốc từ CSDL (bao gồm cả TaiKhoan liên kết)
        NhanVien originalNhanVien = this.getNhanVienByTenDangNhap(tenDangNhap);
        if (originalNhanVien.getTaiKhoan() == null) {
            // Trường hợp hiếm gặp: Nhân viên không có tài khoản?
            throw new RuntimeException("Nhân viên này không có thông tin tài khoản.");
        }
        TaiKhoan taiKhoan = originalNhanVien.getTaiKhoan();

        // 2. Cập nhật các trường thông tin của NhanVien
        originalNhanVien.setHoTen(dataFromForm.getHoTen());
        originalNhanVien.setDiaChi(dataFromForm.getDiaChi());
        originalNhanVien.setSoDienThoai(dataFromForm.getSoDienThoai());

        // 3. Xử lý cập nhật ảnh (chỉ khi người dùng chọn file mới)
        boolean taiKhoanUpdated = false; // Biến cờ để biết có cần lưu TaiKhoan không
        if (anhFile != null && !anhFile.isEmpty()) {
            taiKhoan.setAnh(anhFile.getBytes()); // Đọc dữ liệu file ảnh mới
            taiKhoanUpdated = true; // Đánh dấu là TaiKhoan đã thay đổi
        }

        // 4. Lưu lại các đối tượng đã thay đổi
        nhanVienRepository.save(originalNhanVien); // Lưu thay đổi của NhanVien
        if (taiKhoanUpdated) {
            taiKhoanRepository.save(taiKhoan); // Chỉ lưu TaiKhoan nếu ảnh đã thay đổi
        }
    }
}
