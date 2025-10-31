package com.josephhieu.quanlyquancaphe.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josephhieu.quanlyquancaphe.dto.ChiTietThucDonFormDTO;
import com.josephhieu.quanlyquancaphe.dto.ThucDonFormDTO;
import com.josephhieu.quanlyquancaphe.entity.ChiTietThucDon;
import com.josephhieu.quanlyquancaphe.entity.ThucDon;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.service.HangHoaService;
import com.josephhieu.quanlyquancaphe.service.ThucDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class ThucDonAdminController {

    @Autowired
    private ThucDonService thucDonService;

    @Autowired
    private HangHoaService hangHoaService;



    @Autowired private ObjectMapper objectMapper;

    /**
     * Hiển thị trang Danh sách Thực đơn
     * URL: /admin/thucdon
     */
    @GetMapping("/thucdon")
    public String showThucDonList(Model model) {
        // Lấy danh sách thực đơn đã sắp xếp
        List<ThucDon> dsThucDon = thucDonService.getAllThucDonSorted();

        // Gửi ra view
        model.addAttribute("dsThucDon", dsThucDon);

        // Gửi tín hiệu active cho sidebar
        model.addAttribute("currentPage", "admin_thucdon_list"); // Đặt tên phù hợp

        // Trả về file HTML
        return "admin/thucdon/list"; // templates/admin/thucdon/list.html
    }

    /**
     * PHƯƠNG THỨC MỚI: Hiển thị form Thêm món
     * URL: /admin/thucdon/them
     */
    @GetMapping("/thucdon/them")
    public String showThemThucDonForm(Model model) {
        // Gửi 1 DTO rỗng ra form
        model.addAttribute("thucDonForm", new ThucDonFormDTO());
        // Gửi danh sách nguyên liệu (hàng hóa) để làm dropdown
        model.addAttribute("dsNguyenLieu", hangHoaService.getAllHangHoa());

        model.addAttribute("currentPage", "admin_thucdon_them");
        return "admin/thucdon/form"; // templates/admin/thucdon/form.html
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý lưu món mới
     * URL: /admin/thucdon/save (POST)
     */
    @PostMapping("/thucdon/save")
    public String saveThucDon(
            @ModelAttribute("thucDonForm") ThucDonFormDTO thucDonForm,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            if (thucDonForm.getTenMon() == null || thucDonForm.getTenMon().trim().isEmpty() ||
                    thucDonForm.getGiaTien() == null || thucDonForm.getLoaiMon() == null) {
                throw new IllegalArgumentException("Tên món, Giá tiền và Loại món là bắt buộc.");
            }

            thucDonService.createThucDon(thucDonForm);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm món mới thành công!");
            return "redirect:/admin/thucdon";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Lỗi khi thêm món: " + e.getMessage());
            // Gửi lại dữ liệu form và danh sách nguyên liệu
            model.addAttribute("thucDonForm", thucDonForm);
            model.addAttribute("dsNguyenLieu", hangHoaService.getAllHangHoa());
            model.addAttribute("currentPage", "admin_thucdon_them");
            return "admin/thucdon/form";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Hiển thị trang Tìm kiếm và xử lý
     * URL: /admin/thucdon/timkiem
     */
    @GetMapping("/thucdon/timkiem")
    public String showThucDonSearchPage(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model
    ) {
        // Gọi service để tìm kiếm
        List<ThucDon> dsKetQua = thucDonService.searchThucDon(keyword);

        model.addAttribute("dsKetQua", dsKetQua); // Gửi kết quả
        model.addAttribute("keyword", keyword); // Gửi lại từ khóa
        model.addAttribute("currentPage", "admin_thucdon_timkiem"); // Active sidebar

        return "admin/thucdon/search"; // Trả về file search.html
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý xóa món ăn
     * URL: /admin/thucdon/delete/{id}
     */
    @GetMapping("/thucdon/delete/{id}")
    public String deleteThucDon(@PathVariable("id") String maThucDon, RedirectAttributes redirectAttributes) {
        try {
            thucDonService.deleteThucDon(maThucDon);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa món ăn thành công!");
        } catch (NotFoundException | DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn khi xóa.");
        }
        return "redirect:/admin/thucdon"; // Luôn về trang danh sách
    }

    /**
     * Hiển thị form Chỉnh sửa (ĐÃ SỬA LẠI)
     */
    @GetMapping("/thucdon/edit/{id}")
    public String showEditThucDonForm(@PathVariable("id") String maThucDon, Model model) {
        try {
            // 1. Gọi Service để lấy DTO hoàn chỉnh
            ThucDonFormDTO dto = thucDonService.getThucDonFormDTOById(maThucDon);

            // 2. Gửi DTO ra form
            model.addAttribute("thucDonForm", dto);
            model.addAttribute("dsNguyenLieu", hangHoaService.getAllHangHoa());
            model.addAttribute("currentPage", "admin_thucdon_chinhsua");

            // 3. Gửi JSON của thành phần (vẫn cần cho JavaScript)
            model.addAttribute("thanhPhanJson", objectMapper.writeValueAsString(dto.getThanhPhan()));

            return "admin/thucdon/form";

        } catch (NotFoundException e) {
            return "redirect:/admin/thucdon?error=notFound";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/thucdon?error=unknown";
        }
    }

}
