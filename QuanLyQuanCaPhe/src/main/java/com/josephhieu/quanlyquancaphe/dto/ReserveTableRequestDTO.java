package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) dùng để đóng gói yêu cầu "Đặt bàn".
 * Gửi từ JavaScript (fetch) lên {@link com.josephhieu.quanlyquancaphe.controller.SalesController}
 * khi người dùng điền thông tin vào modal "Đặt bàn" và nhấn "Đặt bàn".
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@NoArgsConstructor // Cần cho Jackson (JSON Deserialization)
public class ReserveTableRequestDTO {

    /**
     * Mã (UUID) của Bàn (bàn trống) được chọn để đặt.
     */
    private String maBan;

    /**
     * Tên khách hàng (từ input "Khách hàng:").
     */
    private String tenKhachHang;

    /**
     * Số điện thoại của khách hàng (tùy chọn).
     */
    private String sdtKhachHang;

    /**
     * Ngày giờ khách hẹn đến.
     * Spring Boot sẽ tự động chuyển đổi chuỗi ISO (ví dụ: "2025-11-03T15:30:00")
     * từ JSON sang đối tượng LocalDateTime.
     */
    private LocalDateTime ngayGioDat;
}