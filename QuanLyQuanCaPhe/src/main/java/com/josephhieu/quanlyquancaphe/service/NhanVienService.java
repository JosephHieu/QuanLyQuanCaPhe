package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import com.josephhieu.quanlyquancaphe.repository.NhanVienRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    public NhanVien getNhanVienByTenDangNhap(String tenDangNhap) {

        return nhanVienRepository.findByTaiKhoan_TenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với tên đăng nhập: " + tenDangNhap));
    }

    @Transactional // Đảm bảo toàn bộ thao tác là một giao dịch
    public void updateNhanVienProfile(String tenDangNhap, NhanVien dataFromForm) {

        // 1. Lấy NhanVien gốc từ CSDL (đầy đủ thông tin)
        NhanVien originalNhanVien = this.getNhanVienByTenDangNhap(tenDangNhap);

        // 2. Chỉ cập nhật những trường được phép thay đổi
        // Chúng ta KHÔNG cập nhật Tên đăng nhập, Mật khẩu, Chức vụ, Lương...
        originalNhanVien.setHoTen(dataFromForm.getHoTen());
        originalNhanVien.setDiaChi(dataFromForm.getDiaChi());
        originalNhanVien.setSoDienThoai(dataFromForm.getSoDienThoai());

        // (Ảnh đại diện sẽ được xử lý riêng nếu bạn làm chức năng upload)

        // 3. Lưu đối tượng gốc đã được cập nhật
        nhanVienRepository.save(originalNhanVien);
    }
}
