package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO (Data Transfer Object) "chỉ đọc" (read-only)
 * dùng để chứa thông tin chi tiết đầy đủ của một bàn khi người dùng
 * nhấn nút "Xem bàn" trong trang Quản lý Bán hàng.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class TableDetailsDTO {

    /**
     * Mã (UUID) của bàn.
     */
    private String maBan;

    /**
     * Tên của bàn (ví dụ: "Bàn 01").
     */
    private String tenBan;

    /**
     * Danh sách các món ăn (đã được gọi) hiện có trên bàn.
     * @see OrderItemDTO
     */
    private List<OrderItemDTO> orderedItems;

    /**
     * Thông tin đặt trước liên quan đến bàn này (nếu có).
     * Sẽ là null nếu bàn không được đặt trước.
     * @see ReservationInfoDTO
     */
    private ReservationInfoDTO reservationInfo;
}