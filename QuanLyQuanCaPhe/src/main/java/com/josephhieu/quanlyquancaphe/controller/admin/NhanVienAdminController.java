package com.josephhieu.quanlyquancaphe.controller.admin;

import com.josephhieu.quanlyquancaphe.entity.NhanVien;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.exception.UsernameAlreadyExistsException;
import com.josephhieu.quanlyquancaphe.service.ChucVuService;
import com.josephhieu.quanlyquancaphe.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            @RequestParam("matKhauMoi") String matKhau,
            @RequestParam("maChucVu") String maChucVu, // Nhận maChucVu
            @RequestParam("anhFile") MultipartFile anhFile,
            Model model
    ) {

        System.out.println(">>> Controller received maChucVu: [" + maChucVu + "]");

        // --- KIỂM TRA MẬT KHẨU (CHO NGƯỜI DÙNG MỚI) ---
        if (matKhau == null || matKhau.trim().isEmpty()) { // <-- SỬA 2: Thêm kiểm tra mật khẩu
            model.addAttribute("passwordError", "Mật khẩu là bắt buộc khi thêm mới."); // Thêm lỗi mật khẩu
            // Gửi lại dữ liệu form
            model.addAttribute("nhanVien", nhanVien);
            model.addAttribute("dsChucVu", chucVuService.getAllChucVu());
            model.addAttribute("currentPage", "admin_nhanvien_them");
            // Giữ lại lỗi tên đăng nhập nếu có
            if (model.containsAttribute("usernameError")) {
                model.addAttribute("usernameError", model.getAttribute("usernameError"));
            }
            // Giữ lại lỗi chức vụ nếu có
            if (model.containsAttribute("chucVuError")) {
                model.addAttribute("chucVuError", model.getAttribute("chucVuError"));
            }
            return "admin/nhanvien/form";
        }

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

    /**
     * PHƯƠNG THỨC MỚI: Xử lý CẬP NHẬT thông tin nhân viên
     * URL: /admin/nhanvien/update (POST)
     */
    @PostMapping("/admin/nhanvien/update")
    public String updateNhanVien(
            @ModelAttribute("nhanVien") NhanVien nhanVienFromForm, // Nhận data từ form (ID, HoTen, DiaChi, SoDienThoai)
            @RequestParam("maChucVu") String maChucVu,
            @RequestParam(value = "matKhauMoi", required = false) String matKhauMoi, // Mật khẩu mới, không bắt buộc
            @RequestParam("anhFile") MultipartFile anhFile,
            Model model // Để xử lý lỗi
    ) {
        try {
            // Gọi service để cập nhật
            nhanVienService.updateNhanVien(
                    nhanVienFromForm.getMaNhanVien(), // Truyền ID vào
                    nhanVienFromForm,
                    maChucVu,
                    matKhauMoi,
                    anhFile
            );
        } catch (NotFoundException e) {
            // Xử lý nếu ID không tìm thấy (hiếm khi xảy ra nếu GET thành công)
            model.addAttribute("errorMessage", e.getMessage());
            // Gửi lại dữ liệu form và ds chức vụ
            model.addAttribute("nhanVien", nhanVienFromForm);
            model.addAttribute("dsChucVu", chucVuService.getAllChucVu());
            model.addAttribute("currentPage", "admin_nhanvien_chinhsua");
            return "admin/nhanvien/form";
        } catch (IOException e) {
            model.addAttribute("fileError", "Lỗi khi xử lý file ảnh!");
            // Gửi lại dữ liệu form và ds chức vụ
            model.addAttribute("nhanVien", nhanVienFromForm);
            model.addAttribute("dsChucVu", chucVuService.getAllChucVu());
            model.addAttribute("currentPage", "admin_nhanvien_chinhsua");
            return "admin/nhanvien/form";
        } catch (RuntimeException e) { // Bắt lỗi khác (VD: Chức vụ không hợp lệ từ Service)
            model.addAttribute("saveError", "Lỗi cập nhật: " + e.getMessage());
            // Gửi lại dữ liệu form và ds chức vụ
            model.addAttribute("nhanVien", nhanVienFromForm); // Dùng data từ form để giữ lại thay đổi
            // Cần lấy lại ChucVu object đầy đủ nếu muốn hiển thị đúng Lương khi có lỗi
            nhanVienService.getNhanVienById(nhanVienFromForm.getMaNhanVien()).ifPresent(nv -> nhanVienFromForm.setChucVu(nv.getChucVu()));
            model.addAttribute("dsChucVu", chucVuService.getAllChucVu());
            model.addAttribute("currentPage", "admin_nhanvien_chinhsua");
            return "admin/nhanvien/form";
        }

        return "redirect:/admin/nhanvien"; // Về trang danh sách
    }

    @GetMapping("/admin/nhanvien/edit/{id}")
    public String showEditNhanVienForm(@PathVariable("id") String maNhanVien, Model model) {
        try {
            // 1. Lấy thông tin nhân viên cần sửa
            NhanVien nhanVien = nhanVienService.getNhanVienById(maNhanVien)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên với mã: " + maNhanVien));

            // 2. Gửi thông tin ra form
            model.addAttribute("nhanVien", nhanVien);

            // 3. Gửi danh sách chức vụ
            model.addAttribute("dsChucVu", chucVuService.getAllChucVu());

            // 4. Đánh dấu trang hiện tại cho sidebar
            model.addAttribute("currentPage", "admin_nhanvien_chinhsua"); // Đặt tên phù hợp với sidebar

            // 5. Trả về file form (dùng chung)
            return "admin/nhanvien/form";

        } catch (NotFoundException e) {
            // Xử lý nếu ID không tồn tại
            // Ví dụ: Chuyển hướng về danh sách với thông báo lỗi
            return "redirect:/admin/nhanvien?error=notFound";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Hiển thị trang Tìm kiếm và xử lý tìm kiếm
     * URL: /admin/nhanvien/timkiem
     */
    @GetMapping("/admin/nhanvien/timkiem")
    public String showNhanVienSearchPage(
            // Nhận từ khóa từ URL (?keyword=...), không bắt buộc
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model
    ) {
        List<NhanVien> ketQuaTimKiem;

        // Gọi service để tìm kiếm
        ketQuaTimKiem = nhanVienService.searchNhanVienByName(keyword);

        // Gửi kết quả (có thể rỗng) ra view
        model.addAttribute("ketQuaTimKiem", ketQuaTimKiem);
        // Gửi lại từ khóa đã nhập để hiển thị lại trên ô input
        model.addAttribute("keyword", keyword);

        // Gửi tín hiệu active cho sidebar
        model.addAttribute("currentPage", "admin_nhanvien_timkiem");

        // Trả về file HTML của trang tìm kiếm
        return "admin/nhanvien/search";
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý xóa nhân viên
     * URL: /admin/nhanvien/delete/{id} (GET)
     */
    @GetMapping("/admin/nhanvien/delete/{id}") // <-- ĐƯỜNG DẪN KHÔNG CÓ /admin
    public String deleteNhanVien(@PathVariable("id") String maNhanVien, RedirectAttributes redirectAttributes) {
        try {
            nhanVienService.deleteNhanVien(maNhanVien);
            // Gửi thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa nhân viên thành công!");
        } catch (NotFoundException | DataIntegrityViolationException e) {
            // Gửi thông báo lỗi (không tìm thấy hoặc lỗi ràng buộc)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Lỗi chung
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn khi xóa.");
        }

        // Luôn chuyển hướng về trang danh sách nhân viên
        return "redirect:/admin/nhanvien";
    }
}
