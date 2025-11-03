package com.josephhieu.quanlyquancaphe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) "chỉ đọc" (read-only)
 * dùng để hiển thị thông tin Đặt trước trong modal "Xem bàn".
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@NoArgsConstructor // Constructor rỗng
@AllArgsConstructor // Constructor đầy đủ tham số
public class ReservationInfoDTO {

    /**
     * Tên khách hàng đã đặt bàn.
     */
    private String tenKhachHang;

    /**
     * Ngày giờ khách hẹn đến (thời gian đặt trước).
     */
    private LocalDateTime ngayGioDat;
}