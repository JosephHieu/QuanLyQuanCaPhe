package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO (Data Transfer Object) "Wrapper" (lớp bao bọc)
 * dùng để chứa kết quả cuối cùng của Báo cáo Thu/Chi.
 * Bao gồm danh sách chi tiết theo ngày và tổng cộng cuối cùng.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@NoArgsConstructor // Constructor rỗng
public class TongThuChiDTO {

    /**
     * Danh sách chi tiết, mỗi phần tử là tổng Thu/Chi của một ngày.
     * @see ThuChiNgayDTO
     */
    private List<ThuChiNgayDTO> chiTietTheoNgay;

    /**
     * Tổng cộng cuối cùng của cột Thu.
     */
    private BigDecimal tongThuCong;

    /**
     * Tổng cộng cuối cùng của cột Chi.
     */
    private BigDecimal tongChiCong;

    /**
     * Constructor đầy đủ tham số
     * Được sử dụng bởi NganSachService sau khi đã tính toán xong.
     *
     * @param chiTiet Danh sách chi tiết theo ngày.
     * @param tongThu Tổng thu.
     * @param tongChi Tổng chi.
     */
    public TongThuChiDTO(List<ThuChiNgayDTO> chiTiet, BigDecimal tongThu, BigDecimal tongChi) {
        this.chiTietTheoNgay = chiTiet;
        this.tongThuCong = tongThu;
        this.tongChiCong = tongChi;
    }
}