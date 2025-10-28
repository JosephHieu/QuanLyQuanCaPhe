package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReserveTableRequestDTO {

    private String maBan;
    private String tenKhachHang;
    private String sdtKhachHang; // Có thể null
    private LocalDateTime ngayGioDat; // Spring tự parse ISO string
}
