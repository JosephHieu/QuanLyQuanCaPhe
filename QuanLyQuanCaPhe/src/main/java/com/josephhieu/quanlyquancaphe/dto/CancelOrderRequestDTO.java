package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) dùng để đóng gói yêu cầu Hủy bàn.
 * Chỉ chứa thông tin ID của bàn cần hủy.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class CancelOrderRequestDTO {
    /**
     * Mã (UUID) của Bàn cần thực hiện thao tác hủy.
     */
    private String maBan;
}