package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) đại diện cho một dòng Chi tiêu
 * trong form "Thêm chi tiêu".
 * Dùng để gửi dữ liệu chi tiêu (cũ và mới) giữa frontend và backend.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Data // Tự động tạo getter, setter, toString, equals, hashCode
@NoArgsConstructor // Tự động tạo constructor rỗng (cần cho Spring data binding)
public class ChiTieuDTO {

    /**
     * Mã (UUID) của khoản chi.
     * Sẽ là null nếu đây là một khoản chi mới (dòng mới thêm).
     * Sẽ có giá trị nếu đây là khoản chi cũ đang được chỉnh sửa.
     */
    private String maChiTieu;

    /**
     * Ngày thực hiện chi tiêu.
     */
    private LocalDate ngayChi;

    /**
     * Tên hoặc mô tả của khoản chi (ví dụ: "Nộp thuế", "Wifi").
     */
    private String tenKhoanChi;

    /**
     * Số tiền đã chi.
     */
    private BigDecimal soTien;
}