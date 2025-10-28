package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CancelOrderRequestDTO {
    private String maBan; // ID bàn cần hủy
}
