package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) dùng để đóng gói yêu cầu "Chuyển bàn"
 * từ JavaScript (fetch) gửi lên {@link com.josephhieu.quanlyquancaphe.controller.SalesController}.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@NoArgsConstructor // Cần cho Jackson (JSON Deserialization)
public class MoveTableRequestDTO {

    /**
     * Mã (UUID) của bàn nguồn (bàn hiện tại có khách).
     */
    private String sourceTableId;

    /**
     * Mã (UUID) của bàn đích (bàn trống).
     */
    private String destinationTableId;
}