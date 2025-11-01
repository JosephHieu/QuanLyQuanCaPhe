package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class TongThuChiDTO {
    private List<ThuChiNgayDTO> chiTietTheoNgay;
    private BigDecimal tongThuCong; // Tổng cộng cột Thu
    private BigDecimal tongChiCong; // Tổng cộng cột Chi

    public TongThuChiDTO(List<ThuChiNgayDTO> chiTiet, BigDecimal tongThu, BigDecimal tongChi) {
        this.chiTietTheoNgay = chiTiet;
        this.tongThuCong = tongThu;
        this.tongChiCong = tongChi;
    }
}