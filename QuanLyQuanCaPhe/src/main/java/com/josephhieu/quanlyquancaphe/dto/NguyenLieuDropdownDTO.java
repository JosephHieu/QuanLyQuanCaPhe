package com.josephhieu.quanlyquancaphe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Tạo constructor có đủ tham số
public class NguyenLieuDropdownDTO {
    private String maHangHoa;
    private String tenHangHoa;
    private String donViTinh; // Chỉ cần Tên đơn vị (String), không cần cả đối tượng
}