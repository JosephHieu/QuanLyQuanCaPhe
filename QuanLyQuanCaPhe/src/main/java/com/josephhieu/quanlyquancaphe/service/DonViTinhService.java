package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.DonViTinh;
import com.josephhieu.quanlyquancaphe.repository.DonViTinhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lớp Service (Nghiệp vụ) cho các chức năng liên quan đến Đơn vị tính ({@link DonViTinh}).
 * Chủ yếu dùng để lấy danh sách Đơn vị tính (kg, gam, lít, v.v.)
 * cho dropdown trong form Thêm/Sửa Hàng hóa.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Service
public class DonViTinhService {

    /**
     * Tiêm (Inject) DonViTinhRepository để truy cập CSDL.
     */
    @Autowired
    private DonViTinhRepository donViTinhRepository;

    /**
     * Lấy danh sách tất cả các Đơn vị tính, sắp xếp theo Tên (tenDonVi).
     *
     * @return một List chứa tất cả đối tượng DonViTinh đã sắp xếp.
     */
    public List<DonViTinh> getAllDonViTinh() {
        // JpaRepository.findAll(Sort) được sử dụng để lấy dữ liệu đã sắp xếp
        return donViTinhRepository.findAll(Sort.by("tenDonVi"));
    }
}