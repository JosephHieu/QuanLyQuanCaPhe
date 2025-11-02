package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.dto.ThuChiNgayDTO;
import com.josephhieu.quanlyquancaphe.dto.TongThuChiDTO;
import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    /**
     * Tạo file Excel (.xls) cho báo cáo Thu Chi
     */
    public ByteArrayInputStream generateThuChiExcel(TongThuChiDTO data) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Tạo một Workbook (.xls)
        try (HSSFWorkbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            // Tạo một Sheet
            HSSFSheet sheet = workbook.createSheet("BaoCaoThuChi");

            // --- 1. Tạo hàng Tiêu đề (Header) ---
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Ngày", "Thu", "Chi"};
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
                // (Thêm style nếu muốn)
            }

            // --- 2. Đổ dữ liệu ---
            int rowIdx = 1;
            for (ThuChiNgayDTO ngay : data.getChiTietTheoNgay()) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(ngay.getNgay().format(formatter));
                row.createCell(1).setCellValue(ngay.getTongThu().doubleValue()); // Chuyển BigDecimal thành double
                row.createCell(2).setCellValue(ngay.getTongChi().doubleValue());
            }

            // --- 3. Thêm dòng Tổng cộng (Footer) ---
            Row footerRow = sheet.createRow(rowIdx);
            footerRow.createCell(0).setCellValue("Tổng cộng");
            footerRow.createCell(1).setCellValue(data.getTongThuCong().doubleValue());
            footerRow.createCell(2).setCellValue(data.getTongChiCong().doubleValue());

            // Tự động điều chỉnh kích thước cột
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            // Ghi workbook vào ByteArrayOutputStream
            workbook.write(out);

            // Trả về file dưới dạng InputStream
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Tạo file Excel (.xls) cho báo cáo Lương
     */
    public ByteArrayInputStream generateLuongExcel(List<NhanVien> dsNhanVien, BigDecimal tongLuong) throws IOException {

        // Tạo một Workbook (.xls)
        try (HSSFWorkbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            HSSFSheet sheet = workbook.createSheet("BaoCaoLuong");

            // --- 1. Tạo hàng Tiêu đề (Header) ---
            Row headerRow = sheet.createRow(0);
            String[] headers = {"STT", "Họ tên", "Chức vụ", "Lương"};
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
                // (Thêm style nếu muốn)
            }

            // --- 2. Đổ dữ liệu ---
            int rowIdx = 1;
            for (NhanVien nv : dsNhanVien) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(rowIdx - 1); // STT (bắt đầu từ 1)
                row.createCell(1).setCellValue(nv.getHoTen());

                String tenChucVu = (nv.getChucVu() != null) ? nv.getChucVu().getTenChucVu() : "N/A";
                BigDecimal luong = (nv.getChucVu() != null && nv.getChucVu().getLuong() != null) ? nv.getChucVu().getLuong() : BigDecimal.ZERO;

                row.createCell(2).setCellValue(tenChucVu);
                row.createCell(3).setCellValue(luong.doubleValue()); // Chuyển BigDecimal thành double
            }

            // --- 3. Thêm dòng Tổng cộng (Footer) ---
            Row footerRow = sheet.createRow(rowIdx);
            footerRow.createCell(0).setCellValue("");
            footerRow.createCell(1).setCellValue("");
            footerRow.createCell(2).setCellValue("Tổng cộng");
            footerRow.createCell(3).setCellValue(tongLuong.doubleValue());

            // Tự động điều chỉnh kích thước cột
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
