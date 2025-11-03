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

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu của Admin
 * liên quan đến nghiệp vụ Quản lý Trang thiết bị (CRUD).
 * Tất cả các URL trong controller này đều có tiền tố /admin.
 *
 * @author Joseph Hieu (Tên của bạn)
 * @version 1.0
 * @since 2025-11-03
 */
@Controller
@RequestMapping("/admin")
public class ThietBiAdminController {

    @Autowired
    private ThietBiService thietBiService;

    /**
     * Hiển thị trang danh sách tất cả thiết bị.
     * Xử lý URL: GET /admin/thietbi
     *
     * @param model Model để truyền danh sách thiết bị (dsThietBi) ra view.
     * @return Tên của file view (template) "admin/thietbi/list".
     */
    @GetMapping("/thietbi")
    public String showThietBiList(Model model) {

        List<ThietBi> dsThietBi = thietBiService.getAllThietBi();

        model.addAttribute("dsThietBi", dsThietBi);

        model.addAttribute("currentPage", "admin_thietbi_list");

        return "admin/thietbi/list";
    }

    /**
     * Xử lý việc lưu một thiết bị mới vào CSDL.
     * Phương thức này được gọi bởi form "Thêm thiết bị".
     * Xử lý URL: POST /admin/thietbi/save
     *
     * @param thietBi Đối tượng ThietBi được bind tự động từ dữ liệu form (th:object).
     * @param model Model để trả về thông báo lỗi và dữ liệu nếu validation thất bại.
     * @return Chuyển hướng về trang danh sách nếu thành công, ngược lại trả về form.
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
     * Hiển thị form để thêm một thiết bị mới.
     * Xử lý URL: GET /admin/thietbi/them
     *
     * @param model Model để truyền một đối tượng ThietBi rỗng ra form.
     * @return Tên của file view (template) "admin/thietbi/form".
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
     * Hiển thị form Chỉnh sửa thiết bị với dữ liệu cũ đã được điền sẵn.
     * Xử lý URL: GET /admin/thietbi/edit/{id}
     *
     * @param maThietBi Mã UUID của thiết bị (lấy từ URL path).
     * @param model Model để truyền đối tượng ThietBi tìm được ra view.
     * @return Tên view template "admin/thietbi/form" (dùng chung).
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
     * Xử lý Cập nhật (lưu) thông tin thiết bị đã chỉnh sửa.
     * Xử lý URL: POST /admin/thietbi/update
     *
     * @param thietBi Đối tượng ThietBi được bind từ form (đã chứa maThietBi).
     * @param model Model để trả về thông báo lỗi nếu thất bại.
     * @return Chuyển hướng về trang danh sách nếu thành công, ngược lại trả về form.
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
     * Xử lý nghiệp vụ Xóa thiết bị.
     * Xử lý URL: GET /admin/thietbi/delete/{id}
     *
     * @param maThietBi Mã UUID của thiết bị cần xóa (lấy từ URL path).
     * @param redirectAttributes Dùng để gửi thông báo (success/error) khi chuyển hướng.
     * @return Chuyển hướng về trang danh sách (/admin/thietbi).
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
