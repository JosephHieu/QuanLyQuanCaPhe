package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.dto.ChiTietThucDonFormDTO;
import com.josephhieu.quanlyquancaphe.dto.ThucDonFormDTO;
import com.josephhieu.quanlyquancaphe.entity.ChiTietThucDon;
import com.josephhieu.quanlyquancaphe.entity.HangHoa;
import com.josephhieu.quanlyquancaphe.entity.ThucDon;
import com.josephhieu.quanlyquancaphe.entity.id.ChiTietThucDonId;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.repository.ChiTietHoaDonRepository;
import com.josephhieu.quanlyquancaphe.repository.ChiTietThucDonRepository;
import com.josephhieu.quanlyquancaphe.repository.HangHoaRepository;
import com.josephhieu.quanlyquancaphe.repository.ThucDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ThucDonService {

    @Autowired
    private ThucDonRepository thucDonRepository;

    @Autowired
    private HangHoaRepository hangHoaRepository;

    @Autowired
    private ChiTietThucDonRepository chiTietThucDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    public List<ThucDon> getAllThucDonSorted() {
        return thucDonRepository.findAllByOrderByLoaiMonAscTenMonAsc();
    }

    /**
     * PHƯƠNG THỨC MỚI: Thêm món ăn mới và thành phần
     */
    @Transactional
    public ThucDon createThucDon(ThucDonFormDTO dto) {
        // 1. Lưu đối tượng ThucDon chính
        ThucDon thucDon = new ThucDon();
        thucDon.setTenMon(dto.getTenMon());
        thucDon.setGiaTienHienTai(dto.getGiaTien());
        thucDon.setLoaiMon(dto.getLoaiMon());

        ThucDon savedThucDon = thucDonRepository.save(thucDon);
        String newThucDonId = savedThucDon.getMaThucDon();

        List<ChiTietThucDon> chiTietList = new ArrayList<>();

        // 2. Lặp qua danh sách thành phần từ DTO
        if (dto.getThanhPhan() != null) {

            for (ChiTietThucDonFormDTO thanhPhanDto : dto.getThanhPhan()) {

                if (thanhPhanDto.getMaHangHoa() == null || thanhPhanDto.getKhoiLuong() == null || thanhPhanDto.getMaHangHoa().isEmpty()) {
                    continue; // Bỏ qua nếu dòng trống (chưa chọn)
                }

                // 3. Tìm HangHoa (nguyên liệu)
                HangHoa hangHoa = hangHoaRepository.findById(thanhPhanDto.getMaHangHoa())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy nguyên liệu: " + thanhPhanDto.getMaHangHoa()));

                // 4. Tạo ID phức hợp
                ChiTietThucDonId chiTietId = new ChiTietThucDonId();
                chiTietId.setMaThucDon(newThucDonId);
                chiTietId.setMaHangHoa(hangHoa.getMaHangHoa());

                // 5. Tạo đối tượng ChiTietThucDon
                ChiTietThucDon chiTiet = new ChiTietThucDon();
                chiTiet.setId(chiTietId);
                chiTiet.setThucDon(savedThucDon);
                chiTiet.setHangHoa(hangHoa);
                chiTiet.setKhoiLuong(thanhPhanDto.getKhoiLuong());
                chiTiet.setDonViTinh(hangHoa.getDonViTinh().getTenDonVi());

                chiTietList.add(chiTiet);
            }
        }

        // 6. Lưu tất cả thành phần
        if (!chiTietList.isEmpty()) {
            chiTietThucDonRepository.saveAll(chiTietList);
        }

        return savedThucDon;
    }

    /**
     * PHƯƠNG THỨC MỚI: Tìm kiếm món ăn
     */
    public List<ThucDon> searchThucDon(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // Nếu không có từ khóa, trả về tất cả (giống trang tìm kiếm kho)
            return getAllThucDonSorted();
        }
        return thucDonRepository.findByTenMonContainingIgnoreCase(keyword.trim());
    }

    /**
     * PHƯƠNG THỨC MỚI: Xóa món ăn
     */
    @Transactional
    public void deleteThucDon(String maThucDon) throws NotFoundException, DataIntegrityViolationException {
        // 1. Kiểm tra món ăn tồn tại
        if (!thucDonRepository.existsById(maThucDon)) {
            throw new NotFoundException("Không tìm thấy món ăn để xóa: " + maThucDon);
        }

        // 2. KIỂM TRA RÀNG BUỘC: Món này đã từng được bán chưa?
        if (chiTietHoaDonRepository.existsByThucDonMaThucDon(maThucDon)) {
            throw new DataIntegrityViolationException("Không thể xóa món này vì đã có trong lịch sử bán hàng (hóa đơn).");
            // (Nếu muốn xóa mềm, bạn sẽ cập nhật 1 trường 'TrangThai' ở đây)
        }

        // 3. Xóa các thành phần (ChiTietThucDon) liên quan
        chiTietThucDonRepository.deleteAllByThucDonMaThucDon(maThucDon);

        // 4. Xóa món ăn (ThucDon)
        thucDonRepository.deleteById(maThucDon);
        System.out.println("Đã xóa thành công món ăn ID: " + maThucDon);
    }

    /**
     * PHƯƠNG THỨC MỚI: Cập nhật món ăn
     */
    @Transactional
    public ThucDon updateThucDon(ThucDonFormDTO dto) throws NotFoundException {
        // 1. Tìm ThucDon gốc
        String maThucDon = dto.getMaThucDon();
        ThucDon thucDon = thucDonRepository.findById(maThucDon)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy món ăn: " + maThucDon));

        // 2. Cập nhật thông tin chính
        thucDon.setTenMon(dto.getTenMon());
        thucDon.setGiaTienHienTai(dto.getGiaTien());
        thucDon.setLoaiMon(dto.getLoaiMon());
        ThucDon savedThucDon = thucDonRepository.save(thucDon);

        // 3. Xóa tất cả thành phần CŨ
        chiTietThucDonRepository.deleteAllByThucDonMaThucDon(maThucDon);

        // 4. Thêm lại các thành phần MỚI (Logic giống hệt createThucDon)
        List<ChiTietThucDon> chiTietList = new ArrayList<>();
        if (dto.getThanhPhan() != null) {
            for (ChiTietThucDonFormDTO thanhPhanDto : dto.getThanhPhan()) {
                if (thanhPhanDto.getMaHangHoa() == null || thanhPhanDto.getKhoiLuong() == null || thanhPhanDto.getMaHangHoa().isEmpty()) {
                    continue;
                }
                HangHoa hangHoa = hangHoaRepository.findById(thanhPhanDto.getMaHangHoa())
                        .orElseThrow(() -> new NotFoundException("Nguyên liệu không hợp lệ: " + thanhPhanDto.getMaHangHoa()));

                ChiTietThucDonId chiTietId = new ChiTietThucDonId();
                chiTietId.setMaThucDon(maThucDon);
                chiTietId.setMaHangHoa(hangHoa.getMaHangHoa());

                ChiTietThucDon chiTiet = new ChiTietThucDon();
                chiTiet.setId(chiTietId);
                chiTiet.setThucDon(savedThucDon);
                chiTiet.setHangHoa(hangHoa);
                chiTiet.setKhoiLuong(thanhPhanDto.getKhoiLuong());
                chiTiet.setDonViTinh(hangHoa.getDonViTinh().getTenDonVi());
                chiTietList.add(chiTiet);
            }
        }

        // 5. Lưu các thành phần mới
        if (!chiTietList.isEmpty()) {
            chiTietThucDonRepository.saveAll(chiTietList);
        }
        return savedThucDon;
    }

    public Optional<ThucDon> getThucDonById(String maThucDon) {
        return thucDonRepository.findById(maThucDon);
    }

    /**
     * PHƯƠNG THỨC MỚI: Lấy đầy đủ thông tin (DTO) cho form Sửa
     */
    @Transactional(readOnly = true) // Chỉ đọc dữ liệu
    public ThucDonFormDTO getThucDonFormDTOById(String maThucDon) throws NotFoundException {
        // 1. Lấy món ăn
        ThucDon thucDon = thucDonRepository.findById(maThucDon)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy món: " + maThucDon));

        // 2. Lấy danh sách thành phần (Logic này được chuyển từ Controller về đây)
        List<ChiTietThucDon> thanhPhanList = chiTietThucDonRepository.findAllByThucDonMaThucDon(maThucDon);

        // 3. Chuyển đổi Entities sang DTO
        ThucDonFormDTO dto = new ThucDonFormDTO();
        dto.setMaThucDon(thucDon.getMaThucDon());
        dto.setTenMon(thucDon.getTenMon());
        dto.setGiaTien(thucDon.getGiaTienHienTai());
        dto.setLoaiMon(thucDon.getLoaiMon());

        // 4. Chuyển danh sách thành phần sang DTO
        List<ChiTietThucDonFormDTO> thanhPhanDtoList = thanhPhanList.stream().map(ct -> {
            ChiTietThucDonFormDTO tpDto = new ChiTietThucDonFormDTO();
            tpDto.setMaHangHoa(ct.getHangHoa().getMaHangHoa());
            tpDto.setKhoiLuong(ct.getKhoiLuong());
            return tpDto;
        }).collect(Collectors.toList());
        dto.setThanhPhan(thanhPhanDtoList);

        return dto; // Trả về DTO hoàn chỉnh
    }
}
