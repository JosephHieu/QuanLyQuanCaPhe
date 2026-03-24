package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.Ban;
import com.josephhieu.quanlyquancaphe.repository.BanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lớp Service (Nghiệp vụ) cho các chức năng liên quan đến Bàn ({@link Ban}).
 * Chủ yếu dùng để lấy thông tin bàn cho trang Quản lý Bán hàng.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Service
public class BanService {

    /**
     * Tiêm (Inject) BanRepository để truy cập CSDL.
     */
    @Autowired
    private BanRepository banRepository;

    /**
     * Lấy danh sách tất cả các Bàn trong quán.
     * (Sau này có thể thêm sắp xếp, ví dụ: Sort.by("tenBan")).
     *
     * @return một List chứa tất cả đối tượng Ban.
     */
    public List<Ban> getAllBan() {
        // JpaRepository.findAll() đã cung cấp sẵn
        return banRepository.findAll();
    }
}