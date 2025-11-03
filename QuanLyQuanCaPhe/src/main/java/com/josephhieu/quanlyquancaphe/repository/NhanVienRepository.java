package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository (Tầng truy cập CSDL) cho Entity {@link NhanVien}.
 * Chứa các phương thức tìm kiếm tùy chỉnh cho nghiệp vụ nhân viên và bảo mật.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 */
@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> { // Entity: NhanVien, Kiểu ID: String (UUID)

    /**
     * Tìm một nhân viên dựa trên Tên đăng nhập của Tài khoản liên kết.
     * Dùng trong CustomUserDetailsService để lấy thông tin NhanVien khi đăng nhập
     * và trong ProfileController để hiển thị trang cá nhân.
     *
     * @param tenDangNhap Tên đăng nhập (username) của TaiKhoan.
     * @return Optional chứa NhanVien nếu tìm thấy.
     */
    Optional<NhanVien> findByTaiKhoan_TenDangNhap(String tenDangNhap);

    /**
     * Tìm danh sách Nhân viên có Họ Tên chứa một từ khóa (không phân biệt hoa/thường).
     * Dùng cho chức năng "Tìm kiếm nhân viên".
     *
     * @param keyword Từ khóa cần tìm kiếm trong HoTen.
     * @return Danh sách NhanVien khớp.
     */
    List<NhanVien> findByHoTenContainingIgnoreCase(String keyword);

    /**
     * Lấy tất cả nhân viên, đồng thời tải (JOIN FETCH) thông tin ChucVu.
     * Đây là câu query đã được tối ưu hóa để giải quyết vấn đề N+1 Query
     * khi hiển thị danh sách nhân viên (lấy Lương, Tên chức vụ).
     *
     * @return Danh sách NhanVien với ChucVu đã được tải.
     */
    @Query("SELECT nv FROM NhanVien nv JOIN FETCH nv.chucVu cv ORDER BY nv.hoTen ASC")
    List<NhanVien> findAllWithChucVu();
}