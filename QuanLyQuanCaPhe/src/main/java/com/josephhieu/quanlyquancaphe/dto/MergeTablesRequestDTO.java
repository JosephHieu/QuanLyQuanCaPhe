package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MergeTablesRequestDTO {

    private List<String> sourceTableIds; // Danh sách ID các bàn cần gộp
    private String destinationTableId; // ID bàn sẽ gộp đến
}
