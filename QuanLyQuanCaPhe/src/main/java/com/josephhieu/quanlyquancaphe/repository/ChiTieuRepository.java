package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ChiTieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import Query
import org.springframework.data.repository.query.Param; // Import Param
import org.springframework.stereotype.Repository;

import java.math.BigDecimal; // Import
import java.time.LocalDate; // Import
import java.util.List; // Import
import java.util.Map; // Import

@Repository
public interface ChiTieuRepository extends JpaRepository<ChiTieu, String> {

    /**
     * PHƯƠNG THỨC MỚI (Thay thế query cũ):
     * Lấy TẤT CẢ chi tiêu trong khoảng ngày.
     */
    List<ChiTieu> findByNgayChiBetween(LocalDate startDate, LocalDate endDate);

    // Lấy các khoản chi gần đây (ví dụ: trong 7 ngày qua)
    List<ChiTieu> findByNgayChiAfterOrderByNgayChiDesc(LocalDate startDate);
}