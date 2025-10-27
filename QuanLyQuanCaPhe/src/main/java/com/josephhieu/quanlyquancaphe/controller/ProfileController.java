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

@Controller
public class ProfileController {

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/profile")
    public String showProfilePage(Model model, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String tenDangNhap = userDetails.getUsername();

        NhanVien nhanVien = nhanVienService.getNhanVienByTenDangNhap(tenDangNhap);

        model.addAttribute("nhanVien", nhanVien);
        model.addAttribute("currentPage", "profile");

        return "profile/view";
    }

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

    // 2. Phương thức MỚI: Hiển thị form CHỈNH SỬA
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

    // 3. Phương thức MỚI: Xử lý CẬP NHẬT (Lưu)
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
