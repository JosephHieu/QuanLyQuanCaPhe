package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) đại diện cho một dòng "Thành phần"
 * (nguyên liệu) trong form Thêm/Sửa Món ăn (Thực đơn).
 * Dùng làm phần tử trong List của {@link ThucDonFormDTO}.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Data
public class ChiTietThucDonFormDTO {

    /**
     * Mã (UUID) của nguyên liệu (Hàng hóa) được chọn từ dropdown.
     * Ví dụ: "uuid-cua-ca-phe", "uuid-cua-sua".
     */
    private String maHangHoa;

    /**
     * Số lượng/khối lượng của nguyên liệu cần dùng.
     * Ví dụ: 5 (gam), 10 (ml).
     */
    private BigDecimal khoiLuong;
}