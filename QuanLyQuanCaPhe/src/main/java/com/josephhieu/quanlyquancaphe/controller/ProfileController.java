package com.josephhieu.quanlyquancaphe.controller;

import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import com.josephhieu.quanlyquancaphe.service.NhanVienService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu liên quan đến
 * trang "Trang cá nhân" (Profile) của người dùng đã đăng nhập.
 * Bao gồm xem, sửa thông tin cá nhân và cập nhật ảnh đại diện.
 *
 * @author Joseph Hieu
 * @version 1.0
 * @since 2025-11-03
 */
@Controller
public class ProfileController {

    @Autowired
    private NhanVienService nhanVienService;

    /**
     * Hiển thị trang "Trang cá nhân" (chỉ xem).
     * Lấy thông tin của người dùng đang đăng nhập và gửi ra view.
     * Xử lý URL: GET /profile
     *
     * @param model Model để truyền đối tượng NhanVien ra view.
     * @param authentication Đối tượng Authentication (từ Spring Security)
     * chứa thông tin của người dùng đang đăng nhập.
     * @return Tên view template "profile/view".
     */
    @GetMapping("/profile")
    public String showProfilePage(Model model, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String tenDangNhap = userDetails.getUsername();

        NhanVien nhanVien = nhanVienService.getNhanVienByTenDangNhap(tenDangNhap);

        model.addAttribute("nhanVien", nhanVien);
        model.addAttribute("currentPage", "profile");

        return "profile/view";
    }

    /**
     * Endpoint API để tải và hiển thị ảnh đại diện (avatar) của người dùng.
     * Được gọi bởi thẻ <img> trong các file HTML (ví dụ: src="/profile/image").
     * Xử lý URL: GET /profile/image
     *
     * @param authentication Đối tượng Authentication để xác định người dùng.
     * @param response       Đối tượng HttpServletResponse để ghi dữ liệu ảnh (byte) trực tiếp vào.
     * @throws IOException Nếu có lỗi khi ghi dữ liệu ảnh.
     */
    @GetMapping("/profile/image")
    @ResponseBody // Báo Spring trả về dữ liệu thô (ảnh), không phải tên file HTML
    public void getUserProfileImage(Authentication authentication, HttpServletResponse response) throws IOException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        NhanVien nhanVien = nhanVienService.getNhanVienByTenDangNhap(userDetails.getUsername());

        byte[] imageBytes = null;
        if (nhanVien != null && nhanVien.getTaiKhoan() != null) {
            imageBytes = nhanVien.getTaiKhoan().getAnh();
        }

        if (imageBytes != null && imageBytes.length > 0) {
            // Nếu có ảnh, đặt kiểu nội dung là ảnh JPEG (hoặc PNG tùy loại ảnh bạn lưu)
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            // Ghi dữ liệu byte của ảnh vào luồng phản hồi
            try (OutputStream os = response.getOutputStream()) {
                os.write(imageBytes);
                os.flush();
            }
        } else {
            // Nếu không có ảnh, bạn có thể trả về lỗi 404 hoặc ảnh mặc định
            // Ở đây ví dụ trả về lỗi 404 Not Found
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Hiển thị form Chỉnh sửa trang cá nhân (với dữ liệu cũ).
     * Xử lý URL: GET /profile/edit
     *
     * @param model Model để truyền đối tượng NhanVien (chứa dữ liệu cũ) ra view.
     * @param authentication Đối tượng Authentication để lấy thông tin người dùng.
     * @return Tên view template "profile/edit".
     */
    @GetMapping("/profile/edit")
    public String showEditProfileForm(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        NhanVien nhanVien = nhanVienService.getNhanVienByTenDangNhap(userDetails.getUsername());

        // Gửi thông tin cũ ra form
        model.addAttribute("nhanVien", nhanVien);
        model.addAttribute("currentPage", "profile");

        // Trả về file HTML mới: "edit.html"
        return "profile/edit";
    }

    /**
     * Xử lý việc Cập nhật (Lưu) thông tin cá nhân.
     * Nhận dữ liệu từ form (Họ tên, Địa chỉ, SĐT) và file ảnh mới.
     * Xử lý URL: POST /profile/update
     *
     * @param nhanVienFromForm Đối tượng NhanVien được bind từ form (chỉ chứa các trường được phép sửa).
     * @param anhFile          File ảnh đại diện mới (MultipartFile).
     * @param authentication   Để xác định đúng người dùng đang cập nhật.
     * @param model            Dùng để trả về thông báo lỗi nếu thất bại.
     * @return Chuyển hướng về trang xem profile (/profile) nếu thành công,
     * ngược lại trả về form edit.
     */
    @PostMapping("/profile/update")
    public String updateProfile(
            @ModelAttribute("nhanVien") NhanVien nhanVienFromForm, // Nhận data từ form (HoTen, DiaChi, SoDienThoai)
            @RequestParam("anhFile") MultipartFile anhFile, // Nhận file ảnh mới (nếu có)
            Authentication authentication, // Để biết ai đang gửi
            Model model // Để gửi lỗi về view nếu cần
    ) {
        // Lấy tên đăng nhập của người dùng hiện tại
        String tenDangNhap = authentication.getName();

        try {
            // Gọi Service để cập nhật (truyền cả file ảnh vào)
            nhanVienService.updateNhanVienProfile(tenDangNhap, nhanVienFromForm, anhFile);

        } catch (IOException e) {
            // Xử lý lỗi nếu không đọc được file ảnh
            model.addAttribute("fileError", "Lỗi khi xử lý file ảnh!");
            // Gửi lại dữ liệu cũ ra form để người dùng xem lại
            NhanVien originalNhanVien = nhanVienService.getNhanVienByTenDangNhap(tenDangNhap);
            model.addAttribute("nhanVien", originalNhanVien);
            model.addAttribute("currentPage", "profile");
            return "profile/edit"; // Trả về trang edit nếu có lỗi

        } catch (RuntimeException e) { // Bắt các lỗi khác (ví dụ: không tìm thấy Nhân viên)
            model.addAttribute("saveError", "Lỗi cập nhật thông tin: " + e.getMessage());
            // Gửi lại dữ liệu cũ ra form
            NhanVien originalNhanVien = nhanVienService.getNhanVienByTenDangNhap(tenDangNhap);
            model.addAttribute("nhanVien", originalNhanVien);
            model.addAttribute("currentPage", "profile");
            return "profile/edit";
        }

        // Chuyển hướng về trang XEM thông tin sau khi lưu thành công
        return "redirect:/profile";
    }
}
