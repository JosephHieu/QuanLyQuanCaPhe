package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class TableDetailsDTO {
    private String maBan; // Có thể thêm các thông tin khác của bàn nếu cần
    private String tenBan;
    private List<OrderItemDTO> orderedItems;
    private ReservationInfoDTO reservationInfo;
}
