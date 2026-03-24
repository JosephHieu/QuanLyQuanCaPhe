package com.josephhieu.quanlyquancaphe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO (Data Transfer Object) đại diện cho một Nguyên liệu (Hàng hóa).
 * Dùng để truyền tải thông tin cơ bản của nguyên liệu một cách an toàn,
 * đặc biệt là khi gửi dữ liệu lên frontend (ví dụ: cho dropdowns).
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Data // Tự động tạo getter, setter, toString, equals, hashCode
@AllArgsConstructor // Tự động tạo constructor với tất cả các tham số
public class NguyenLieuDTO {
    /**
     * Mã (UUID) của Hàng hóa (nguyên liệu).
     */
    private String maHangHoa;

    /**
     * Tên của Hàng hóa (nguyên liệu).
     */
    private String tenHangHoa;

    /**
     * Tên Đơn vị tính (ví dụ: "gam", "ml") - là kiểu String.
     */
    private String donViTinh;
}