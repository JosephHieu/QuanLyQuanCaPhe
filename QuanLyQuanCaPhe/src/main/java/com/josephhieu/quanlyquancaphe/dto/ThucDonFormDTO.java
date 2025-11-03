package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO (Data Transfer Object) chính dùng cho form "Thêm/Sửa Món ăn".
 * Đóng gói tất cả dữ liệu từ form (thông tin món chính và danh sách thành phần)
 * để gửi lên Controller.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
public class ThucDonFormDTO {

    /**
     * Mã (UUID) của món ăn.
     * Sẽ là null khi "Thêm mới".
     * Sẽ có giá trị khi "Chỉnh sửa".
     */
    private String maThucDon;

    /**
     * Tên của món ăn.
     */
    private String tenMon;

    /**
     * Giá bán hiện tại của món ăn.
     */
    private BigDecimal giaTien;

    /**
     * Loại món (ví dụ: "Cafe", "Trà", "Bánh").
     */
    private String loaiMon;

    /**
     * Danh sách các thành phần (nguyên liệu) của món ăn.
     * Spring Boot sẽ tự động bind (liên kết) các input động
     * (ví dụ: thanhPhan[0].maHangHoa) vào danh sách này.
     * @see ChiTietThucDonFormDTO
     */
    private List<ChiTietThucDonFormDTO> thanhPhan = new ArrayList<>();
}