package com.josephhieu.quanlyquancaphe.exception;

/**
 * Một {@link RuntimeException} tùy chỉnh (Custom Exception) cụ thể.
 * Được ném (throw) ra trong nghiệp vụ "Thêm nhân viên" (hoặc "Đăng ký")
 * khi người dùng cố gắng tạo một
 * {@link com.josephhieu.quanlyquancaphe.entity.TaiKhoan} (Tài khoản)
 * mới với một `TenDangNhap` (Tên đăng nhập) đã tồn tại trong cơ sở dữ liệu.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    /**
     * Constructor tạo một exception với một thông báo lỗi cụ thể.
     * @param message Thông báo lỗi (ví dụ: "Tên đăng nhập 'admin' đã tồn tại!").
     */
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}