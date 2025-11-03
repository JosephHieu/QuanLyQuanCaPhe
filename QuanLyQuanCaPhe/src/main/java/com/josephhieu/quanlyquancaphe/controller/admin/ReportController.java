package com.josephhieu.quanlyquancaphe.controller.admin;

import com.josephhieu.quanlyquancaphe.dto.TongThuChiDTO;
import com.josephhieu.quanlyquancaphe.service.ExcelExportService;
import com.josephhieu.quanlyquancaphe.service.NganSachService;
import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import com.josephhieu.quanlyquancaphe.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu của Admin
 * liên quan đến nghiệp vụ Thống kê và Báo cáo.
 * Các URL đều có tiền tố /admin.
 *
 * @author Joseph Hieu
 * @version 1.0
 * @since 2025-11-03
 */
@Controller
@RequestMapping("/admin")
public class ReportController {

    @Autowired
    private NganSachService nganSachService;

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private ExcelExportService excelExportService;

    /**
     * Hiển thị trang Thống kê & Báo cáo chính.
     * Phương thức này xử lý logic để hiển thị các loại báo cáo khác nhau (Thu/Chi, Lương, v.v.)
     * dựa trên tham số 'reportType' từ URL.
     * Xử lý URL: GET /admin/reports
     *
     * @param reportType Loại báo cáo người dùng chọn (ví dụ: "Tất cả", "Lương"). Mặc định là "Tất cả".
     * @param startDate  Ngày bắt đầu lọc (không bắt buộc).
     * @param endDate    Ngày kết thúc lọc (không bắt buộc).
     * @param model      Model để truyền dữ liệu báo cáo (DTO, dsNhanVien) và cờ (isLuongReport) ra view.
     * @return Tên view template "admin/report/view".
     */
    @GetMapping("/reports")
    public String showReportPage(
            @RequestParam(value = "reportType", required = false, defaultValue = "Tất cả") String reportType,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model
    ) {
        // 1. Đặt ngày mặc định
        if (startDate == null) startDate = LocalDate.now().minusDays(7);
        if (endDate == null) endDate = LocalDate.now();

        // 2. Đặt cờ (flag)
        boolean isThuChiReport = false;
        boolean isLuongReport = false;
        // (Thêm cờ cho các loại báo cáo khác sau)

        // 3. Xử lý logic dựa trên reportType
        if ("Lương".equals(reportType)) {
            // --- XỬ LÝ BÁO CÁO LƯƠNG ---
            isLuongReport = true; // Bật cờ Lương
            List<NhanVien> dsNhanVien = nhanVienService.getAllNhanVien();
            BigDecimal tongLuong = dsNhanVien.stream()
                    .map(nv -> nv.getChucVu() != null ? nv.getChucVu().getLuong() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("dsNhanVien", dsNhanVien);
            model.addAttribute("tongLuong", tongLuong);

        } else {
            // --- MẶC ĐỊNH: BÁO CÁO THU CHI (cho "Tất cả", "Nhập-xuất", "Bán hàng", "Nhập", "Xuất", "Phí khác") ---
            isThuChiReport = true; // Bật cờ Thu Chi
            TongThuChiDTO thuChiData = nganSachService.getTongHopThuChi(startDate, endDate);
            model.addAttribute("dto", thuChiData);
        }

        // 4. Gửi cờ ra view
        model.addAttribute("isThuChiReport", isThuChiReport);
        model.addAttribute("isLuongReport", isLuongReport);

        // 5. Gửi lại các lựa chọn lọc
        model.addAttribute("reportTypeSelected", reportType);
        model.addAttribute("startDateSelected", startDate);
        model.addAttribute("endDateSelected", endDate);

        // 6. Gửi tín hiệu active sidebar
        model.addAttribute("currentPage", "admin_reports");

        return "admin/report/view";
    }

    /**
     * Xử lý yêu cầu xuất file báo cáo (ví dụ: Excel).
     * Tạo file dựa trên các bộ lọc (reportType, dates, exportFormat) và trả về cho trình duyệt.
     * Xử lý URL: GET /admin/reports/export
     *
     * @param reportType   Loại báo cáo cần xuất.
     * @param startDate    Ngày bắt đầu.
     * @param endDate      Ngày kết thúc.
     * @param exportFormat Định dạng file (ví dụ: "xls", "txt").
     * @return Một {@link ResponseEntity} chứa {@link Resource} (file) để tải về.
     */
    @GetMapping("/reports/export")
    public ResponseEntity<Resource> exportReport(
            @RequestParam(value = "reportType", defaultValue = "Tất cả") String reportType,
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "exportFormat") String exportFormat
    ) {

        // Tạo tên file (ví dụ: "BaoCao_20251102.xls")
        String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String filename = "BaoCao_" + reportType + "_" + timestamp;

        ByteArrayInputStream in = null;
        String contentType = "application/octet-stream"; // Mặc định

        try {
            // === XỬ LÝ THEO LOẠI BÁO CÁO ===

            if ("Lương".equals(reportType)) {
                // --- XUẤT BÁO CÁO LƯƠNG ---
                List<NhanVien> dsNhanVien = nhanVienService.getAllNhanVien();
                BigDecimal tongLuong = dsNhanVien.stream()
                        .map(nv -> (nv.getChucVu() != null && nv.getChucVu().getLuong() != null) ? nv.getChucVu().getLuong() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if ("xls".equalsIgnoreCase(exportFormat)) {
                    in = excelExportService.generateLuongExcel(dsNhanVien, tongLuong);
                    filename += ".xls";
                    contentType = "application/vnd.ms-excel";
                }
                // (Thêm else if cho TXT, SQL nếu muốn)

            } else if ("Tất cả".equals(reportType) || "Nhập-xuất".equals(reportType)) {
                // --- XUẤT BÁO CÁO THU CHI ---
                TongThuChiDTO thuChiData = nganSachService.getTongHopThuChi(startDate, endDate);

                if ("xls".equalsIgnoreCase(exportFormat)) {
                    in = excelExportService.generateThuChiExcel(thuChiData);
                    filename += ".xls";
                    contentType = "application/vnd.ms-excel";
                }
                // (Thêm else if cho TXT, SQL nếu muốn)
            }
            // (Thêm else if cho các loại báo cáo khác...)

            // === KẾT THÚC XỬ LÝ ===

            if (in == null) {
                in = new ByteArrayInputStream(("Chuc nang xuat file cho loai bao cao '" + reportType + "' chua duoc ho tro.").getBytes());
                filename += ".txt";
                contentType = "text/plain";
            }

            InputStreamResource resource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}