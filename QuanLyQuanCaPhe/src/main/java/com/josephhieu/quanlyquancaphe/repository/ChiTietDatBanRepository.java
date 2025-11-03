package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ChiTietDatBan;
import com.josephhieu.quanlyquancaphe.entity.id.ChiTietDatBanId; // <-- PHẢI IMPORT LỚP ID
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link ChiTietDatBan}.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Repository
public interface ChiTietDatBanRepository extends JpaRepository<ChiTietDatBan, ChiTietDatBanId> { // <-- SỬA LẠI KIỂU ID THÀNH ChiTietDatBanId

    /**
     * Tìm ChiTietDatBan (chi tiết đặt bàn/hóa đơn đang hoạt động)
     * dựa trên Mã Bàn và Trạng Thái của Hóa Đơn liên kết.
     *
     * @param maBan Mã (UUID) của Bàn cần tìm.
     * @param trangThaiHoaDon Trạng thái của hóa đơn (false = 0 = chưa thanh toán).
     * @return Optional chứa ChiTietDatBan nếu tìm thấy.
     */
    Optional<ChiTietDatBan> findByBanMaBanAndHoaDonTrangThai(String maBan, boolean trangThaiHoaDon);
}