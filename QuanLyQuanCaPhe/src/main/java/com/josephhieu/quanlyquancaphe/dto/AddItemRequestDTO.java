package com.josephhieu.quanlyquancaphe.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AddItemRequestDTO {

    private String maBan;
    private List<ItemToAddDTO> items;

    @Data
    @NoArgsConstructor
    public static class ItemToAddDTO {
        private String maThucDon;
        private int soLuong;
    }
}
