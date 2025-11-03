package com.josephhieu.quanlyquancaphe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO (Data Transfer Object) "chỉ đọc" (read-only)
 * dùng để chứa thông tin tinh gọn của Hàng hóa (Nguyên liệu)
 * để hiển thị trong các dropdown (ô chọn) ở frontend.
 *
 * Mục đích chính là để giải quyết lỗi Lazy Loading (Hibernate Proxy)
 * khi chuyển đổi danh sách Entity (HangHoa -> DonViTinh) sang JSON
 * để gửi cho JavaScript.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@AllArgsConstructor // Được sử dụng trong Service (stream().map(...))
public class NguyenLieuDropdownDTO {

    /**
     * Mã (UUID) của Hàng hóa (nguyên liệu).
     */
    private String maHangHoa;

    /**
     * Tên của Hàng hóa (nguyên liệu).
     */
    private String tenHangHoa;

    /**
     * Tên Đơn vị tính (ví dụ: "gam", "ml", "kg")
     * (Đây là kiểu String, không phải đối tượng Entity, để tránh lỗi Lazy Loading).
     */
    private String donViTinh;
}