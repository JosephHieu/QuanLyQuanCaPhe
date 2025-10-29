package com.josephhieu.quanlyquancaphe.controller.admin;

import com.josephhieu.quanlyquancaphe.entity.ThietBi;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.service.ThietBiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ThietBiAdminController {

    @Autowired
    private ThietBiService thietBiService;

    /**
     * Hiển thị trang Danh sách Thiết bị
     * URL: /admin/thietbi
     */
    @GetMapping("/thietbi")
    public String showThietBiList(Model model) {

        List<ThietBi> dsThietBi = thietBiService.getAllThietBi();

        model.addAttribute("dsThietBi", dsThietBi);

        model.addAttribute("currentPage", "admin_thietbi_list");

        return "admin/thietbi/list";
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý lưu thiết bị mới
     * URL: /admin/thietbi/save (POST)
     */
    @PostMapping("/thietbi/save")
    public String saveThietBi(@ModelAttribute("thietBi") ThietBi thietBi, Model model) {
        try {
            // (Thêm validation ở đây nếu cần, ví dụ kiểm tra các trường bắt buộc)
            if (thietBi.getTenThietBi() == null || thietBi.getTenThietBi().trim().isEmpty() ||
                    thietBi.getNgayMua() == null || thietBi.getSoLuong() <= 0 || thietBi.getDonGiaMua() == null) {
                throw new IllegalArgumentException("Vui lòng điền đầy đủ các trường bắt buộc (*).");
            }

            thietBiService.saveThietBi(thietBi);
            // Chuyển hướng về trang danh sách sau khi lưu thành công
            return "redirect:/admin/thietbi";

        } catch (IllegalArgumentException e) {
            // Nếu có lỗi validation, trả về form với thông báo lỗi
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("thietBi", thietBi); // Gửi lại dữ liệu đã nhập
            model.addAttribute("currentPage", "admin_thietbi_them");
            return "admin/thietbi/form";
        } catch (Exception e) {
            // Xử lý các lỗi khác
            e.printStackTrace(); // In lỗi ra console
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi lưu thiết bị.");
            model.addAttribute("thietBi", thietBi);
            model.addAttribute("currentPage", "admin_thietbi_them");
            return "admin/thietbi/form";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Hiển thị form Thêm thiết bị
     * URL: /admin/thietbi/them
     */
    @GetMapping("/thietbi/them")
    public String showThemThietBiForm(Model model) {
        // Gửi một đối tượng ThietBi rỗng ra form
        model.addAttribute("thietBi", new ThietBi());
        // Gửi tín hiệu active cho sidebar
        model.addAttribute("currentPage", "admin_thietbi_them");
        // Trả về file HTML của form
        return "admin/thietbi/form";
    }

    /**
     * PHƯƠNG THỨC MỚI: Show Edit Equipment Form
     * URL: /admin/thietbi/edit/{id}
     */
    @GetMapping("/thietbi/edit/{id}")
    public String showEditThietBiForm(@PathVariable("id") String maThietBi, Model model) {
        try {
            ThietBi thietBi = thietBiService.getThietBiById(maThietBi)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy thiết bị với mã: " + maThietBi));

            model.addAttribute("thietBi", thietBi); // Send existing data to the form
            model.addAttribute("currentPage", "admin_thietbi_chinhsua"); // Set current page for sidebar

            return "admin/thietbi/form"; // Reuse the same form template

        } catch (NotFoundException e) {
            return "redirect:/admin/thietbi?error=notFound"; // Redirect if ID not found
        }
    }

    /**
     * XỬ LÝ CẬP NHẬT THIẾT BỊ
     * URL: /admin/thietbi/update (POST)
     */
    @PostMapping("/admin/thietbi/update")
    public String updateThietBi(@ModelAttribute("thietBi") ThietBi thietBi, Model model) {
        // @ModelAttribute đã tự động lấy maThietBi từ input hidden
        try {
            // Thêm validation tương tự như khi lưu mới
            if (thietBi.getTenThietBi() == null || thietBi.getTenThietBi().trim().isEmpty() ||
                    thietBi.getNgayMua() == null || thietBi.getSoLuong() <= 0 || thietBi.getDonGiaMua() == null) {
                throw new IllegalArgumentException("Vui lòng điền đầy đủ các trường bắt buộc (*).");
            }

            // Gọi service save, JPA sẽ tự động update vì có ID
            thietBiService.saveThietBi(thietBi);
            return "redirect:/admin/thietbi"; // Về trang danh sách

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("thietBi", thietBi); // Gửi lại dữ liệu đã sửa
            model.addAttribute("currentPage", "admin_thietbi_chinhsua");
            return "admin/thietbi/form"; // Trả về form nếu lỗi
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật thiết bị.");
            model.addAttribute("thietBi", thietBi);
            model.addAttribute("currentPage", "admin_thietbi_chinhsua");
            return "admin/thietbi/form";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý xóa thiết bị
     * URL: /admin/thietbi/delete/{id} (GET)
     */
    @GetMapping("/thietbi/delete/{id}")
    public String deleteThietBi(@PathVariable("id") String maThietBi, RedirectAttributes redirectAttributes) {
        try {
            thietBiService.deleteThietBiById(maThietBi);
            // Thêm thông báo thành công để hiển thị trên trang danh sách
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa thiết bị thành công!");
        } catch (NotFoundException e) {
            // Thêm thông báo lỗi không tìm thấy
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            // Thêm thông báo lỗi do ràng buộc
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Bắt các lỗi khác
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa thiết bị.");
        }
        // Luôn chuyển hướng về trang danh sách
        return "redirect:/admin/thietbi";
    }
}
