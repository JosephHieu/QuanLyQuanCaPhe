package com.josephhieu.quanlyquancaphe.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO (Data Transfer Object) dùng để đóng gói yêu cầu Thêm món ăn (hoặc Cập nhật)
 * từ modal "Chọn thực đơn" (trang Quản lý Bán hàng) gửi lên Controller.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class AddItemRequestDTO {

    /**
     * Mã (UUID) của Bàn mà đơn hàng được thêm vào.
     */
    private String maBan;

    /**
     * Danh sách các món ăn cần thêm/cập nhật.
     */
    private List<ItemToAddDTO> items;

    /**
     * Lớp DTO con, đại diện cho một món ăn và số lượng cụ thể
     * trong yêu cầu thêm/cập nhật.
     */
    @Data
    @NoArgsConstructor
    public static class ItemToAddDTO {
        private String maThucDon;
        private int soLuong;
    }
}
