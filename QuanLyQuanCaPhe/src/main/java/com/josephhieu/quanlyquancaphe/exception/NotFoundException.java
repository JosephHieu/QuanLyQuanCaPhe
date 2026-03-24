package com.josephhieu.quanlyquancaphe.exception;

/**
 * Một {@link RuntimeException} tùy chỉnh (Custom Exception) phổ biến.
 * Được ném (throw) ra khi một Service (ví dụ: NhanVienService, ThucDonService)
 * cố gắng tìm kiếm một đối tượng (Entity) bằng ID hoặc một khóa duy nhất khác
 * nhưng không tìm thấy kết quả nào trong cơ sở dữ liệu.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructor tạo một exception với một thông báo lỗi cụ thể.
     * @param message Thông báo lỗi (ví dụ: "Không tìm thấy nhân viên với mã: 123").
     */
    public NotFoundException(String message) {
        super(message); // Gọi constructor của lớp cha (RuntimeException)
    }

    /**
     * Constructor tạo một exception với thông báo lỗi và nguyên nhân gốc (Throwable).
     * @param message Thông báo lỗi.
     * @param cause Nguyên nhân gốc (ví dụ: một SQLException).
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}