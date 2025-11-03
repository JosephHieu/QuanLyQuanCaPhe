package com.josephhieu.quanlyquancaphe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Date; // Thêm import cho constructor

/**
 * DTO (Data Transfer Object) đại diện cho một hàng (row)
 * trong bảng báo cáo Thu/Chi.
 * Lưu trữ tổng Thu và tổng Chi cho một Ngày cụ thể.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Data
@NoArgsConstructor // Constructor rỗng
public class ThuChiNgayDTO {

    /**
     * Ngày diễn ra Thu/Chi.
     */
    private LocalDate ngay;

    /**
     * Tổng số tiền Thu trong ngày (từ HoaDon).
     */
    private BigDecimal tongThu = BigDecimal.ZERO;

    /**
     * Tổng số tiền Chi trong ngày (từ ChiTieu).
     */
    private BigDecimal tongChi = BigDecimal.ZERO;

    /**
     * Constructor đầy đủ 3 tham số.
     * Được sử dụng bởi NganSachService khi gộp dữ liệu.
     *
     * @param ngay Ngày.
     * @param tongThu Tổng thu.
     * @param tongChi Tổng chi.
     */
    public ThuChiNgayDTO(LocalDate ngay, BigDecimal tongThu, BigDecimal tongChi) {
        this.ngay = ngay;
        this.tongThu = (tongThu != null) ? tongThu : BigDecimal.ZERO;
        this.tongChi = (tongChi != null) ? tongChi : BigDecimal.ZERO;
    }

    /**
     * Constructor 2 tham số (cho query JPQL của HoaDonRepository).
     * Được gọi bởi `SELECT new ... (LocalDate, BigDecimal)`.
     *
     * @param ngay Ngày (từ FUNCTION('DATE', h.ngayGioTao)).
     * @param tongThu Tổng thu (từ SUM(h.tongTien)).
     */
    public ThuChiNgayDTO(LocalDate ngay, BigDecimal tongThu) {
        this.ngay = ngay;
        this.tongThu = (tongThu != null) ? tongThu : BigDecimal.ZERO;
        this.tongChi = BigDecimal.ZERO; // Mặc định tổng chi là 0
    }

    /**
     * Constructor 2 tham số (cho query JPQL, xử lý kiểu Long).
     * Được gọi nếu `SUM()` trả về `Long`.
     *
     * @param ngay Ngày.
     * @param tongThu Tổng thu (dạng Long).
     */
    public ThuChiNgayDTO(LocalDate ngay, Long tongThu) {
        this.ngay = ngay;
        this.tongThu = (tongThu != null) ? BigDecimal.valueOf(tongThu) : BigDecimal.ZERO;
        this.tongChi = BigDecimal.ZERO;
    }

    /**
     * Constructor 2 tham số (cho query JPQL, xử lý kiểu java.sql.Date).
     * Đề phòng trường hợp `FUNCTION('DATE', ...)` trả về `java.sql.Date`.
     *
     * @param ngay Ngày (dạng java.sql.Date).
     * @param tongThu Tổng thu (dạng BigDecimal).
     */
    public ThuChiNgayDTO(Date ngay, BigDecimal tongThu) {
        this.ngay = (ngay != null) ? ngay.toLocalDate() : null;
        this.tongThu = (tongThu != null) ? tongThu : BigDecimal.ZERO;
        this.tongChi = BigDecimal.ZERO;
    }

    /**
     * Constructor 2 tham số (cho query JPQL, xử lý java.sql.Date và Long).
     *
     * @param ngay Ngày (dạng java.sql.Date).
     * @param tongThu Tổng thu (dạng Long).
     */
    public ThuChiNgayDTO(Date ngay, Long tongThu) {
        this.ngay = (ngay != null) ? ngay.toLocalDate() : null;
        this.tongThu = (tongThu != null) ? BigDecimal.valueOf(tongThu) : BigDecimal.ZERO;
        this.tongChi = BigDecimal.ZERO;
    }
}