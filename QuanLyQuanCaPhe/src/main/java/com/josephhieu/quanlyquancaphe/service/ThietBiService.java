package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.ThietBi;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.repository.ThietBiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ThietBiService {

    @Autowired
    private ThietBiRepository thietBiRepository;

    public List<ThietBi> getAllThietBi() {
        return thietBiRepository.findAll(Sort.by("tenThietBi"));
    }

    /**
     * Lấy thiết bị theo ID
     */
    public Optional<ThietBi> getThietBiById(String maThietBi) {
        return thietBiRepository.findById(maThietBi);
    }

    /**
     * PHƯƠNG THỨC MỚI: Lưu thiết bị (Thêm mới hoặc Cập nhật)
     */
    @Transactional // Đảm bảo lưu thành công
    public ThietBi saveThietBi(ThietBi thietBi) {

        return thietBiRepository.save(thietBi);
    }

    /**
     * PHƯƠNG THỨC MỚI: Xóa thiết bị theo ID
     */
    @Transactional
    public void deleteThietBiById(String maThietBi) throws NotFoundException, DataIntegrityViolationException {
        // Kiểm tra xem thiết bị có tồn tại không
        if (!thietBiRepository.existsById(maThietBi)) {
            throw new NotFoundException("Không tìm thấy thiết bị để xóa: " + maThietBi);
        }
        try {
            // Thực hiện xóa
            thietBiRepository.deleteById(maThietBi);
            System.out.println("Đã xóa thiết bị có ID: " + maThietBi);
        } catch (DataIntegrityViolationException e) {
            // Bắt lỗi nếu thiết bị đang được sử dụng ở bảng khác (ví dụ: DonNhap)
            System.err.println("Lỗi xóa thiết bị do ràng buộc khóa ngoại: " + e.getMessage());
            throw new DataIntegrityViolationException("Không thể xóa thiết bị này vì đang được sử dụng.");
        }
    }
}
