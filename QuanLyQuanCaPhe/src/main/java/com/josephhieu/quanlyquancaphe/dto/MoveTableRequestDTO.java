package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoveTableRequestDTO {

    private String sourceTableId; // ID bàn hiện tại
    private String destinationTableId; // ID bàn chuyển đến
}
