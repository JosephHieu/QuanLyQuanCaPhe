package com.josephhieu.quanlyquancaphe.controller.admin;

import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import com.josephhieu.quanlyquancaphe.exception.UsernameAlreadyExistsException;
import com.josephhieu.quanlyquancaphe.service.ChucVuService;
import com.josephhieu.quanlyquancaphe.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class NhanVienAdminController {

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private ChucVuService chucVuService;

    @PostMapping("/admin/nhanvien/save")
    public String saveNhanVien(
            @ModelAttribute("nhanVien") NhanVien nhanVien,
            @RequestParam("tenDangNhap") String tenDangNhap,
            @RequestParam("matKhau") String matKhau,
            @RequestParam("maChucVu") String maChucVu, // Nhận maChucVu
            @RequestParam("anhFile") MultipartFile anhFile,
            Model model
    ) {

        System.out.println(">>> Controller received maChucVu: [" + maChucVu + "]");


        // --- KIỂM TRA CHỨC VỤ ---
        if (maChucVu == null || maChucVu.isEmpty()) {
            model.addAttribute("chucVuError", "Vui lòng chọn chức vụ."); // Thêm lỗi chức vụ
            // Gửi lại dữ liệu form
            model.addAttribute("nhanVien", nhanVien);
            model.addAttribute("dsChucVu", chucVuService.getAllChucVu());
            model.addAttribute("currentPage", "admin_nhanvien_them");
            // (Nếu có lỗi tên đăng nhập thì cũng gửi lại)
            if (model.containsAttribute("usernameError")) {
                model.addAttribute("usernameError", model.getAttribute("usernameError"));
            }
            return "admin/nhanvien/form";
        }

        try {
            // Gọi Service với maChucVu đã được kiểm tra
            nhanVienService.createNhanVien(nhanVien, tenDangNhap, matKhau, maChucVu, anhFile);

        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("usernameError", e.getMessage());
            // Gửi lại dữ liệu form
            model.addAttribute("nhanVien", nhanVien);
            model.addAttribute("dsChucVu", chucVuService.getAllChucVu());
            model.addAttribute("currentPage", "admin_nhanvien_them");
            return "admin/nhanvien/form";
        } catch (IOException e) {
            model.addAttribute("fileError", "Lỗi khi đọc file ảnh!");
            // Gửi lại dữ liệu form
            model.addAttribute("nhanVien", nhanVien);
            model.addAttribute("dsChucVu", chucVuService.getAllChucVu());
            model.addAttribute("currentPage", "admin_nhanvien_them");
            return "admin/nhanvien/form";
        } catch (DataIntegrityViolationException e) { // Lỗi phổ biến khi lưu CSDL (VD: khóa ngoại sai, unique constraint)
            System.err.println("!!! DATABASE SAVE ERROR: " + e.getMessage()); // In lỗi thật ra console
            model.addAttribute("saveError", "Lỗi lưu dữ liệu. Vui lòng kiểm tra lại thông tin."); // Thông báo chung chung hơn
            // ... (Gửi lại form) ...
            return "admin/nhanvien/form";
        } catch (RuntimeException e) { // Bắt lỗi nếu Service vẫn không tìm thấy ChucVu
            model.addAttribute("chucVuError", "Chức vụ không hợp lệ. Vui lòng thử lại.");
            // Gửi lại dữ liệu form
            model.addAttribute("nhanVien", nhanVien);
            model.addAttribute("dsChucVu", chucVuService.getAllChucVu());
            model.addAttribute("currentPage", "admin_nhanvien_them");
            return "admin/nhanvien/form";
        }

        return "redirect:/admin/nhanvien";
    }

    @GetMapping("/admin/nhanvien/them")
    public String showThemNhanVienForm(Model model) {

        // 1. Gửi 1 đối tượng NhanVien rỗng
        model.addAttribute("nhanVien", new NhanVien());

        // 2. Gửi danh sách chức vụ để làm dropdown
        model.addAttribute("dsChucVu", chucVuService.getAllChucVu());

        // 3. Gửi tín hiệu active cho sidebar
        model.addAttribute("currentPage", "admin_nhanvien_them");

        // 4. Trả về file HTML của form
        return "admin/nhanvien/form";
    }

    @GetMapping("/admin/nhanvien")
    public String showNhanVienList(Model model) {

        List<NhanVien> danhSachNhanVien = nhanVienService.getAllNhanVien();

        model.addAttribute("danhSachNhanVien", danhSachNhanVien);

        model.addAttribute("currentPage", "admin_nhanvien_list");

        return "admin/nhanvien/list";
    }
}
