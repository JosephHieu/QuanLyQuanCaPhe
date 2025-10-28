package com.josephhieu.quanlyquancaphe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInfoDTO {
    private String tenKhachHang;
    private LocalDateTime ngayGioDat;

}
