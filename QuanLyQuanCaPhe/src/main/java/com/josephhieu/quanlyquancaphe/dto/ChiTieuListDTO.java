package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class ChiTieuListDTO {
    // Tên này phải khớp với tên dùng trong Thymeleaf
    private List<ChiTieuDTO> danhSachChiTieu = new ArrayList<>();
}