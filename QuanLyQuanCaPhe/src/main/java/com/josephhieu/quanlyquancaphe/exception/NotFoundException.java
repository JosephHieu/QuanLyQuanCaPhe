package com.josephhieu.quanlyquancaphe.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message); // Gọi constructor của lớp cha (RuntimeException)
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause); // Constructor với nguyên nhân gốc (nếu cần)
    }
}
