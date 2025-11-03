package com.josephhieu.quanlyquancaphe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) đại diện cho một Món hàng (Item)
 * trong một đơn hàng (Order).
 * Dùng để hiển thị chi tiết món ăn trong modal "Xem bàn"
 * và cũng dùng trong các yêu cầu cập nhật đơn hàng.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data // Tự động tạo getter, setter, v.v.
@NoArgsConstructor // Constructor rỗng
@AllArgsConstructor // Constructor đầy đủ tham số
public class OrderItemDTO {

    /**
     * Mã (UUID) của món ăn (ThucDon).
     * Cần thiết cho JavaScript để xác định món ăn khi cập nhật/tách bàn.
     */
    private String maThucDon;

    /**
     * Tên món ăn (ví dụ: "Cà phê sữa").
     */
    private String tenMon;

    /**
     * Số lượng món ăn đã gọi.
     */
    private int soLuong;
}