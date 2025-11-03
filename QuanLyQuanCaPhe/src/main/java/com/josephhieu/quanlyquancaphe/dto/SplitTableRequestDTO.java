package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO (Data Transfer Object) dùng để đóng gói yêu cầu "Tách bàn".
 * Gửi từ JavaScript (fetch) lên {@link com.josephhieu.quanlyquancaphe.controller.SalesController}
 * khi người dùng xác nhận modal Tách bàn.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@NoArgsConstructor // Cần cho Jackson (JSON Deserialization)
public class SplitTableRequestDTO {

    /**
     * Mã (UUID) của bàn nguồn (bàn đang có khách/món).
     */
    private String sourceTableId;

    /**
     * Mã (UUID) của bàn đích (bàn trống) sẽ được tách món sang.
     */
    private String destinationTableId;

    /**
     * Danh sách các món ăn và số lượng cụ thể cần tách sang bàn đích.
     */
    private List<SplitItemDTO> items;

    /**
     * Lớp DTO con, đại diện cho một món ăn và số lượng cụ thể
     * trong yêu cầu tách bàn.
     */
    @Data
    @NoArgsConstructor
    public static class SplitItemDTO {
        /**
         * Mã (UUID) của món ăn (ThucDon).
         */
        private String maThucDon;

        /**
         * Số lượng của món ăn đó cần tách sang bàn mới.
         */
        private int soLuong;
    }
}