package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class SplitTableRequestDTO {

    private String sourceTableId;
    private String destinationTableId;
    private List<SplitItemDTO> items; // Danh sách món cần tách

    @Data
    @NoArgsConstructor
    public static class SplitItemDTO {
        private String maThucDon;
        private int soLuong; // Số lượng cần tách đi
    }
}
