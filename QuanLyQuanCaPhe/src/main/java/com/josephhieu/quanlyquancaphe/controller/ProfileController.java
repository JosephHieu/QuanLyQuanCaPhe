package com.josephhieu.quanlyquancaphe.controller;

import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import com.josephhieu.quanlyquancaphe.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
            @ModelAttribute("nhanVien") NhanVien nhanVienFromForm, // Nhận data từ form
            Authentication authentication // Để biết ai đang gửi
    ) {
        // Lấy tên đăng nhập của người dùng hiện tại
        String tenDangNhap = authentication.getName();

        // Gọi Service để cập nhật
        nhanVienService.updateNhanVienProfile(tenDangNhap, nhanVienFromForm);

        // Chuyển hướng về trang XEM sau khi lưu thành công
        return "redirect:/profile";
    }
}
