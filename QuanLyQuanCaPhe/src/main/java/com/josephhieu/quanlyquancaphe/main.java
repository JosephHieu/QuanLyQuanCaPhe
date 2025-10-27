package com.josephhieu.quanlyquancaphe;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class main {
    public static void main(String[] args) {
        // 1. Tạo đối tượng mã hóa BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 2. Mật khẩu gốc bạn muốn mã hóa
        String rawPassword = "123456";

        // 3. Thực hiện mã hóa
        String encodedPassword = encoder.encode(rawPassword);

        // 4. In kết quả mã hóa ra màn hình
        System.out.println("Mật khẩu gốc: " + rawPassword);
        System.out.println("Mật khẩu đã mã hóa (BCrypt): " + encodedPassword);
    }
}
