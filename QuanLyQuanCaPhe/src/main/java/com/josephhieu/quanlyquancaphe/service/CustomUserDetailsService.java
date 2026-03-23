package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.TaiKhoan;
import com.josephhieu.quanlyquancaphe.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Lớp Service tùy chỉnh, triển khai (implements) {@link UserDetailsService} của Spring Security.
 * Nhiệm vụ cốt lõi của lớp này là cung cấp logic để Spring Security
 * tìm một người dùng ({@link TaiKhoan}) trong CSDL bằng Tên đăng nhập
 * và chuyển đổi nó thành một đối tượng {@link UserDetails} mà Spring Security có thể hiểu được.
 *
 * @author Joseph Hieu
 * @version 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Tiêm (Inject) TaiKhoanRepository để tìm kiếm tài khoản trong CSDL.
     */
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    /**
     * Phương thức này được Spring Security tự động gọi khi người dùng cố gắng đăng nhập.
     *
     * @param tenDangNhap Tên đăng nhập (username) mà người dùng nhập vào form.
     * @return một đối tượng UserDetails chứa tên, mật khẩu (đã mã hóa), và quyền hạn.
     * @throws UsernameNotFoundException Nếu không tìm thấy tài khoản với tenDangNhap tương ứng.
     */
    @Override
    public UserDetails loadUserByUsername(String tenDangNhap) throws UsernameNotFoundException {

        // 1. Tìm TaiKhoan trong CSDL bằng Tên đăng nhập.
        // Nếu không tìm thấy, ném ra exception để Spring Security biết là đăng nhập thất bại.
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new UsernameNotFoundException("Tên đăng nhập không tồn tại: " + tenDangNhap));

        // 2. Chuyển đổi QuyenHan (String, ví dụ: "Admin", "Staff") thành
        // một Set các GrantedAuthority (mà Spring Security yêu cầu).
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Thêm tiền tố "ROLE_" là quy ước chuẩn của Spring Security.
        // Ví dụ: "Admin" -> "ROLE_ADMIN"
        String role = "ROLE_" + taiKhoan.getQuyenHan().toUpperCase();
        authorities.add(new SimpleGrantedAuthority(role));

        // 3. Trả về đối tượng User (một implement của UserDetails)
        // Spring Security sẽ tự động lấy mật khẩu từ đây để so sánh
        // với mật khẩu người dùng nhập (đã được mã hóa).
        return User.builder()
                .username(taiKhoan.getTenDangNhap())
                .password(taiKhoan.getMatKhau()) // Mật khẩu đã được mã hóa trong CSDL
                .authorities(authorities) // Danh sách quyền
                .build();
    }
}
