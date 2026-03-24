package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.dto.ThuChiNgayDTO;
import com.josephhieu.quanlyquancaphe.dto.TongThuChiDTO;
import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import org.apache.poi.hssf.usermodel.HSSFSheet; // Dùng HSSF cho định dạng .xls (Excel 97-2003)
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

/**
 * Lớp Service chịu trách nhiệm tạo các file Excel (định dạng .xls)
 * cho chức năng "Xuất file" trong Báo cáo.
 * Sử dụng thư viện Apache POI.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Service
public class ExcelExportService {

    /**
     * Tạo file Excel (.xls) cho báo cáo Thu Chi từ dữ liệu đã tổng hợp.
     *
     * @param data DTO chứa danh sách Thu/Chi theo ngày và Tổng cộng.
     * @return Một {@link ByteArrayInputStream} chứa dữ liệu của file .xls.
     * @throws IOException Nếu có lỗi khi ghi dữ liệu vào workbook.
     */
    public ByteArrayInputStream generateThuChiExcel(TongThuChiDTO data) throws IOException {

        // Định dạng ngày (ví dụ: 23/12/2014)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Sử dụng try-with-resources để tự động đóng HSSFWorkbook và ByteArrayOutputStream
        try (HSSFWorkbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            // Tạo một Sheet (trang tính) mới
            HSSFSheet sheet = workbook.createSheet("BaoCaoThuChi");

            // --- 1. Tạo hàng Tiêu đề (Header) ---
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Ngày", "Thu", "Chi"};
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
                // (Có thể thêm CellStyle ở đây để in đậm, tô màu...)
            }

            // --- 2. Đổ dữ liệu chi tiết ---
            int rowIdx = 1; // Bắt đầu từ hàng 1 (sau header)
            for (ThuChiNgayDTO ngay : data.getChiTietTheoNgay()) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(ngay.getNgay().format(formatter));
                row.createCell(1).setCellValue(ngay.getTongThu().doubleValue()); // Excel xử lý double
                row.createCell(2).setCellValue(ngay.getTongChi().doubleValue());
            }

            // --- 3. Thêm dòng Tổng cộng (Footer) ---
            Row footerRow = sheet.createRow(rowIdx);
            footerRow.createCell(0).setCellValue("Tổng cộng");
            footerRow.createCell(1).setCellValue(data.getTongThuCong().doubleValue());
            footerRow.createCell(2).setCellValue(data.getTongChiCong().doubleValue());
            // (Thêm CellStyle cho dòng này để in đậm)

            // Tự động điều chỉnh kích thước cột cho vừa nội dung
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            // Ghi toàn bộ Workbook (file Excel) vào luồng byte trong bộ nhớ
            workbook.write(out);

            // Trả về một InputStream từ mảng byte đã ghi
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    /**
     * Tạo file Excel (.xls) cho báo cáo Lương nhân viên.
     *
     * @param dsNhanVien Danh sách các đối tượng NhanVien (đã join ChucVu).
     * @param tongLuong Tổng lương của tất cả nhân viên.
     * @return Một {@link ByteArrayInputStream} chứa dữ liệu của file .xls.
     * @throws IOException Nếu có lỗi khi ghi dữ liệu vào workbook.
     */
    public ByteArrayInputStream generateLuongExcel(List<NhanVien> dsNhanVien, BigDecimal tongLuong) throws IOException {

        try (HSSFWorkbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            HSSFSheet sheet = workbook.createSheet("BaoCaoLuong");

            // --- 1. Tạo hàng Tiêu đề (Header) ---
            Row headerRow = sheet.createRow(0);
            String[] headers = {"STT", "Họ tên", "Chức vụ", "Lương"};
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
            }

            // --- 2. Đổ dữ liệu ---
            int rowIdx = 1;
            for (NhanVien nv : dsNhanVien) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(rowIdx - 1); // STT (bắt đầu từ 1)
                row.createCell(1).setCellValue(nv.getHoTen());

                // Lấy thông tin từ ChucVu (đã được join fetch)
                String tenChucVu = (nv.getChucVu() != null) ? nv.getChucVu().getTenChucVu() : "N/A";
                BigDecimal luong = (nv.getChucVu() != null && nv.getChucVu().getLuong() != null) ? nv.getChucVu().getLuong() : BigDecimal.ZERO;

                row.createCell(2).setCellValue(tenChucVu);
                row.createCell(3).setCellValue(luong.doubleValue());
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