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

/**
 * Lớp Service (Nghiệp vụ) cho các chức năng liên quan đến Khuyến mãi ({@link KhuyenMai}).
 * Bao gồm logic CRUD và xử lý ràng buộc khóa ngoại với HoaDon.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Service
public class KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    /**
     * Lấy tất cả các chương trình khuyến mãi, sắp xếp theo Ngày bắt đầu (mới nhất trước).
     *
     * @return Danh sách KhuyenMai đã sắp xếp.
     */
    public List<KhuyenMai> getAllKhuyenMai() {
        return khuyenMaiRepository.findAll(Sort.by(Sort.Direction.DESC, "ngayBatDau"));
    }

    /**
     * Lưu (Thêm mới hoặc Cập nhật) một chương trình khuyến mãi.
     * Tự động gán LoaiKhuyenMai = "Phần trăm" và TrangThai = true
     * dựa trên logic của form "Thêm khuyến mãi".
     *
     * @param khuyenMai Đối tượng KhuyenMai cần lưu.
     * @return Đối tượng KhuyenMai đã được lưu.
     * @throws IllegalArgumentException Nếu Tên hoặc Ngày không hợp lệ.
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

        // Gán giá trị mặc định dựa trên form "Thêm"
        // Nếu là form "Sửa", các giá trị này sẽ bị ghi đè bởi dữ liệu từ form
        if (khuyenMai.getMaKhuyenMai() == null) { // Chỉ gán mặc định khi Thêm mới
            khuyenMai.setLoaiKhuyenMai("Phần trăm");
            khuyenMai.setTrangThai(true); // Tự động kích hoạt
        }

        return khuyenMaiRepository.save(khuyenMai);
    }

    /**
     * Lấy thông tin một Khuyến mãi bằng ID (UUID).
     *
     * @param maKhuyenMai Mã (UUID) của khuyến mãi.
     * @return Optional<KhuyenMai> chứa khuyến mãi nếu tìm thấy.
     */
    public Optional<KhuyenMai> getKhuyenMaiById(String maKhuyenMai) {
        return khuyenMaiRepository.findById(maKhuyenMai);
    }

    /**
     * Xóa một chương trình khuyến mãi.
     * Trước khi xóa, phương thức này sẽ tìm tất cả các Hóa đơn
     * đang sử dụng khuyến mãi này và gỡ bỏ liên kết (set MaKhuyenMai = null)
     * để tránh lỗi ràng buộc khóa ngoại (DataIntegrityViolationException).
     *
     * @param maKhuyenMai Mã (UUID) của khuyến mãi cần xóa.
     * @throws NotFoundException Nếu không tìm thấy khuyến mãi.
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
        // --- KẾT THÚC XỬ LÝ ---

        // 3. Bây giờ mới xóa khuyến mãi (sẽ an toàn)
        khuyenMaiRepository.deleteById(maKhuyenMai);
        System.out.println("Đã xóa khuyến mãi ID: " + maKhuyenMai);
    }

    /**
     * Tìm kiếm khuyến mãi dựa trên từ khóa (keyword).
     * Nếu từ khóa rỗng, trả về tất cả khuyến mãi.
     *
     * @param keyword Từ khóa tìm kiếm.
     * @return Danh sách KhuyenMai khớp.
     */
    public List<KhuyenMai> searchKhuyenMai(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllKhuyenMai();
        }
        return khuyenMaiRepository.findByTenKhuyenMaiContainingIgnoreCase(keyword.trim());
    }
}