package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor // Cần cho các thư viện
public class ThuChiNgayDTO {
    private LocalDate ngay;
    private BigDecimal tongThu = BigDecimal.ZERO;
    private BigDecimal tongChi = BigDecimal.ZERO;

    // Constructor 3 tham số (Dùng cho Service)
    public ThuChiNgayDTO(LocalDate ngay, BigDecimal tongThu, BigDecimal tongChi) {
        this.ngay = ngay;
        this.tongThu = (tongThu != null) ? tongThu : BigDecimal.ZERO;
        this.tongChi = (tongChi != null) ? tongChi : BigDecimal.ZERO;
    }

    // (Xóa các constructor 2 tham số khác ở đây)
}