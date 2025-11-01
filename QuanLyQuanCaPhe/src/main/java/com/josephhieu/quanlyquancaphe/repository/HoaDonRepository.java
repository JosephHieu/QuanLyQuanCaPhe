package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.dto.ThuChiNgayDTO;
import com.josephhieu.quanlyquancaphe.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,String> {

    List<HoaDon> findByKhuyenMaiMaKhuyenMai(String maKhuyenMai);

    /**
     * PHƯƠNG THỨC MỚI (Thay thế query cũ):
     * Lấy TẤT CẢ hóa đơn (đã thanh toán) trong khoảng thời gian.
     */
    List<HoaDon> findByTrangThaiTrueAndNgayGioTaoBetween(LocalDateTime startDate, LocalDateTime endDate);

}
