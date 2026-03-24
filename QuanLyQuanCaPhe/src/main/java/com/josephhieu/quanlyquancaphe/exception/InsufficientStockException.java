package com.josephhieu.quanlyquancaphe.exception;

/**
 * Một {@link RuntimeException} tùy chỉnh (Custom Exception).
 * Được ném (throw) ra khi một nghiệp vụ cố gắng xuất hoặc bán một
 * {@link com.josephhieu.quanlyquancaphe.entity.HangHoa} (Hàng hóa)
 * nhưng số lượng tồn kho (SoLuong) không đủ.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
public class InsufficientStockException extends RuntimeException {
    /**
     * Constructor tạo một exception với một thông báo lỗi cụ thể.
     * @param message Thông báo lỗi (ví dụ: "Không đủ số lượng tồn kho cho 'Cafe'").
     */
    public InsufficientStockException(String message) {
        super(message);
    }
}