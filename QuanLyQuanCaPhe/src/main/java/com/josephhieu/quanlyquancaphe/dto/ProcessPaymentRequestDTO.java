package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) dùng để đóng gói yêu cầu "Thanh toán".
 * Gửi từ JavaScript (fetch) lên {@link com.josephhieu.quanlyquancaphe.controller.SalesController}
 * khi người dùng nhấn nút "Thanh toán" trong modal.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@NoArgsConstructor // Cần cho Jackson (JSON Deserialization)
public class ProcessPaymentRequestDTO {

    /**
     * Mã (UUID) của Bàn đang được thanh toán.
     */
    private String maBan;

    /**
     * Cờ (flag) từ checkbox "Đổi trạng thái bàn sang trống...".
     * True nếu người dùng muốn bàn tự động chuyển về "Trống" sau khi thanh toán.
     * False nếu muốn giữ nguyên trạng thái (ví dụ: "Có khách").
     */
    private boolean resetTable;
}