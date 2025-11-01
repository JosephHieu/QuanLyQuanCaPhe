package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.HoaDon;
import com.josephhieu.quanlyquancaphe.entity.KhuyenMai;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.repository.HoaDonRepository;
import com.josephhieu.quanlyquancaphe.repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    public List<KhuyenMai> getAllKhuyenMai() {

        return khuyenMaiRepository.findAll(Sort.by(Sort.Direction.DESC, "ngayBatDau"));
    }

    /**
     * Lưu khuyến mãi (Thêm mới hoặc Cập nhật)
     */
    @Transactional
    public KhuyenMai saveKhuyenMai(KhuyenMai khuyenMai) {
        // Validation cơ bản
        if (khuyenMai.getTenKhuyenMai() == null || khuyenMai.getTenKhuyenMai().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi là bắt buộc.");
        }
        if (khuyenMai.getNgayBatDau() == null || khuyenMai.getNgayKetThuc() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và kết thúc là bắt buộc.");
        }
        if (khuyenMai.getNgayBatDau().isAfter(khuyenMai.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu.");
        }

        khuyenMai.setLoaiKhuyenMai("Phần trăm");
        khuyenMai.setTrangThai(true); // Tự động kích hoạt

        return khuyenMaiRepository.save(khuyenMai);
    }

    /**
     * PHƯƠNG THỨC MỚI (hoặc kiểm tra đã có): Lấy khuyến mãi theo ID
     */
    public Optional<KhuyenMai> getKhuyenMaiById(String maKhuyenMai) {
        return khuyenMaiRepository.findById(maKhuyenMai);
    }

    /**
     * CẬP NHẬT PHƯƠNG THỨC XÓA: Xóa khuyến mãi
     */
    @Transactional
    public void deleteKhuyenMai(String maKhuyenMai) throws NotFoundException {
        if (!khuyenMaiRepository.existsById(maKhuyenMai)) {
            throw new NotFoundException("Không tìm thấy khuyến mãi để xóa: " + maKhuyenMai);
        }

        // --- XỬ LÝ KHÓA NGOẠI TRƯỚC KHI XÓA ---
        // 1. Tìm tất cả Hóa đơn đang dùng khuyến mãi này
        List<HoaDon> hoaDonsToUpdate = hoaDonRepository.findByKhuyenMaiMaKhuyenMai(maKhuyenMai);

        // 2. Gỡ bỏ khuyến mãi khỏi các hóa đơn đó (set MaKhuyenMai = null)
        for (HoaDon hoaDon : hoaDonsToUpdate) {
            hoaDon.setKhuyenMai(null);
            hoaDonRepository.save(hoaDon);
        }

        // 3. Bây giờ mới xóa khuyến mãi (sẽ an toàn)
        khuyenMaiRepository.deleteById(maKhuyenMai);
        System.out.println("Đã xóa khuyến mãi ID: " + maKhuyenMai);

    }

    public List<KhuyenMai> searchKhuyenMai(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllKhuyenMai();
        }
        return khuyenMaiRepository.findByTenKhuyenMaiContainingIgnoreCase(keyword.trim());
    }
}
