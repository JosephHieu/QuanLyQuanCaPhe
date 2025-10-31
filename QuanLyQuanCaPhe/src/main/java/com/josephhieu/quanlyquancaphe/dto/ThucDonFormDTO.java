package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ThucDonFormDTO {

    private String maThucDon;
    private String tenMon;
    private BigDecimal giaTien;
    private String loaiMon;
    private List<ChiTietThucDonFormDTO> thanhPhan = new ArrayList<>();
}
