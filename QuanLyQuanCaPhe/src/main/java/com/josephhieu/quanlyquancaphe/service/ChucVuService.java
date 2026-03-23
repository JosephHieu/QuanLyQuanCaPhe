package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.ChucVu;
import com.josephhieu.quanlyquancaphe.repository.ChucVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lớp Service (Nghiệp vụ) cho các chức năng liên quan đến Chức vụ ({@link ChucVu}).
 * Chủ yếu dùng để lấy danh sách Chức vụ cho dropdown
 * trong form Thêm/Sửa Nhân viên.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Service
public class ChucVuService {

    /**
     * Tiêm (Inject) ChucVuRepository để truy cập CSDL.
     */
    @Autowired
    private ChucVuRepository chucVuRepository;

    /**
     * Lấy danh sách tất cả các Chức vụ có trong hệ thống.
     *
     * @return một List chứa tất cả đối tượng ChucVu.
     */
    public List<ChucVu> getAllChucVu() {
        // JpaRepository.findAll() đã cung cấp sẵn
        return chucVuRepository.findAll();
    }
}
