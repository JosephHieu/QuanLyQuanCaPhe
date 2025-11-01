package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ChiTieuDTO {
    private String maChiTieu; // Cần ID để biết là sửa hay thêm mới
    private LocalDate ngayChi;
    private String tenKhoanChi;
    private BigDecimal soTien;
}