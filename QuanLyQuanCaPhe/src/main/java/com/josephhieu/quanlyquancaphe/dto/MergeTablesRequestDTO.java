package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO (Data Transfer Object) dùng để đóng gói yêu cầu "Gộp bàn"
 * từ JavaScript (fetch) gửi lên {@link com.josephhieu.quanlyquancaphe.controller.SalesController}.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@NoArgsConstructor // Cần cho Jackson (JSON Deserialization)
public class MergeTablesRequestDTO {

    /**
     * Danh sách các Mã (UUID) của các bàn nguồn (bàn bị gộp).
     */
    private List<String> sourceTableIds;

    /**
     * Mã (UUID) của bàn đích (bàn được gộp vào).
     */
    private String destinationTableId;
}