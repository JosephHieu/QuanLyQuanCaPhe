package com.josephhieu.quanlyquancaphe.controller.admin;

import com.josephhieu.quanlyquancaphe.entity.KhuyenMai;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/marketing")
public class MarketingAdminController {

    @Autowired
    private KhuyenMaiService khuyenMaiService;

    /**
     * Hiển thị trang Danh sách Khuyến mãi
     * URL: /admin/marketing
     */
    @GetMapping("")
    public String showKhuyenMaiList(Model model) {
        List<KhuyenMai> dsKhuyenMai = khuyenMaiService.getAllKhuyenMai();
        model.addAttribute("dsKhuyenMai", dsKhuyenMai);

        // Gửi tín hiệu active cho sidebar
        model.addAttribute("currentPage", "admin_marketing_list");

        // Trả về file HTML
        return "admin/marketing/list"; // templates/admin/marketing/list.html
    }

    /**
     * PHƯƠNG THỨC MỚI: Hiển thị form Thêm khuyến mãi
     * URL: /admin/marketing/them
     */
    @GetMapping("/them")
    public String showThemKhuyenMaiForm(Model model) {
        // Gửi một đối tượng KhuyenMai rỗng ra form
        model.addAttribute("khuyenMai", new KhuyenMai());
        // Gửi tín hiệu active cho sidebar
        model.addAttribute("currentPage", "admin_marketing_them");
        // Trả về file HTML của form
        return "admin/marketing/form"; // templates/admin/marketing/form.html
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý lưu khuyến mãi mới
     * URL: /admin/marketing/save (POST)
     */
    @PostMapping("/save")
    public String saveKhuyenMai(
            @ModelAttribute("khuyenMai") KhuyenMai khuyenMai,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // (Service đã có validation cơ bản)
            // Giao diện chỉ có "% giảm giá", nên ta gán giá trị đó vào "GiaTriGiam"
            // (Nếu form có các loại khác, logic ở đây sẽ phức tạp hơn)
            khuyenMaiService.saveKhuyenMai(khuyenMai);

            redirectAttributes.addFlashAttribute("successMessage", "Thêm khuyến mãi thành công!");
            return "redirect:/admin/marketing"; // Về trang danh sách

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("khuyenMai", khuyenMai); // Gửi lại dữ liệu đã nhập
            model.addAttribute("currentPage", "admin_marketing_them");
            return "admin/marketing/form";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi lưu khuyến mãi.");
            model.addAttribute("khuyenMai", khuyenMai);
            model.addAttribute("currentPage", "admin_marketing_them");
            return "admin/marketing/form";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Hiển thị form Sửa khuyến mãi
     * URL: /admin/marketing/edit/{id}
     */
    @GetMapping("/edit/{id}")
    public String showEditKhuyenMaiForm(@PathVariable("id") String maKhuyenMai, Model model) {
        try {
            KhuyenMai khuyenMai = khuyenMaiService.getKhuyenMaiById(maKhuyenMai)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy khuyến mãi: " + maKhuyenMai));

            model.addAttribute("khuyenMai", khuyenMai);
            model.addAttribute("currentPage", "admin_marketing_sua"); // Đặt tên khớp sidebar
            return "admin/marketing/form"; // Dùng chung form

        } catch (NotFoundException e) {
            return "redirect:/admin/marketing?error=notFound";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý CẬP NHẬT khuyến mãi
     * URL: /admin/marketing/update (POST)
     */
    @PostMapping("/update")
    public String updateKhuyenMai(
            @ModelAttribute("khuyenMai") KhuyenMai khuyenMai,
            Model model, RedirectAttributes redirectAttributes
    ) {
        try {
            // (Service saveKhuyenMai đã có validation)
            // (Service saveKhuyenMai sẽ tự động update vì khuyenMai có ID)
            khuyenMaiService.saveKhuyenMai(khuyenMai);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật khuyến mãi thành công!");
            return "redirect:/admin/marketing";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("khuyenMai", khuyenMai); // Gửi lại dữ liệu đã sửa
            model.addAttribute("currentPage", "admin_marketing_sua");
            return "admin/marketing/form";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật.");
            model.addAttribute("khuyenMai", khuyenMai);
            model.addAttribute("currentPage", "admin_marketing_sua");
            return "admin/marketing/form";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý xóa khuyến mãi
     * URL: /admin/marketing/delete/{id} (GET)
     */
    @GetMapping("/delete/{id}")
    public String deleteKhuyenMai(@PathVariable("id") String maKhuyenMai, RedirectAttributes redirectAttributes) {
        try {
            khuyenMaiService.deleteKhuyenMai(maKhuyenMai);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa khuyến mãi thành công!");
        } catch (NotFoundException | DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn khi xóa.");
        }

        // Luôn chuyển hướng về trang danh sách
        return "redirect:/admin/marketing";
    }
}
