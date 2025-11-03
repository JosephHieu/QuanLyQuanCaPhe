package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

/**
 * DTO "Wrapper" (lớp bao bọc) dùng để nhận một danh sách (List) các {@link ChiTieuDTO}
 * từ form "Thêm chi tiêu" động.
 *
 * Spring MVC và Thymeleaf bind (liên kết) dữ liệu form động (thêm/xóa dòng)
 * vào một List bên trong một đối tượng dễ dàng hơn là bind vào một List trần.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
public class ChiTieuListDTO {

    /**
     * Danh sách các khoản chi tiêu.
     * Tên biến "danhSachChiTieu" phải khớp chính xác với
     * thuộc tính 'name' trong thẻ input của Thymeleaf, ví dụ:
     * th:field="*{danhSachChiTieu[__${iterStat.index}__].ngayChi}"
     */
    private List<ChiTieuDTO> danhSachChiTieu = new ArrayList<>();
}