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

/**
 * Lớp Service (Nghiệp vụ) cho các chức năng liên quan đến Thiết bị ({@link ThietBi}).
 * Bao gồm logic CRUD cho quản lý thiết bị.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Service
public class ThietBiService {

    @Autowired
    private ThietBiRepository thietBiRepository;

    /**
     * Lấy tất cả thiết bị, sắp xếp theo Tên (tenThietBi).
     *
     * @return Danh sách {@link ThietBi}.
     */
    public List<ThietBi> getAllThietBi() {
        return thietBiRepository.findAll(Sort.by("tenThietBi"));
    }

    /**
     * Lấy thông tin một Thiết bị bằng ID (UUID).
     *
     * @param maThietBi Mã (UUID) của thiết bị.
     * @return Optional<ThietBi> chứa thiết bị nếu tìm thấy.
     */
    public Optional<ThietBi> getThietBiById(String maThietBi) {
        return thietBiRepository.findById(maThietBi);
    }

    /**
     * Lưu (Thêm mới hoặc Cập nhật) một Thiết bị.
     * JPA sẽ tự động nhận diện là Thêm mới (nếu ID null) hoặc Cập nhật (nếu ID đã tồn tại).
     *
     * @param thietBi Đối tượng ThietBi cần lưu.
     * @return Đối tượng ThietBi đã được lưu.
     */
    @Transactional // Đảm bảo lưu thành công
    public ThietBi saveThietBi(ThietBi thietBi) {

        return thietBiRepository.save(thietBi);
    }

    /**
     * Xóa một Thiết bị dựa trên ID (UUID).
     *
     * @param maThietBi Mã (UUID) của thiết bị cần xóa.
     * @throws NotFoundException Nếu không tìm thấy thiết bị.
     * @throws DataIntegrityViolationException Nếu thiết bị đang được sử dụng
     * (ví dụ: đã có trong {@link // DonNhap}).
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
