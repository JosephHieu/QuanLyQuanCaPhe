package com.josephhieu.quanlyquancaphe.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josephhieu.quanlyquancaphe.dto.ChiTietThucDonFormDTO;
import com.josephhieu.quanlyquancaphe.dto.NguyenLieuDTO;
import com.josephhieu.quanlyquancaphe.dto.NguyenLieuDropdownDTO;
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
        model.addAttribute("thucDonForm", new ThucDonFormDTO());

        // SỬA LẠI: Lấy DTO thay vì Entity
        List<NguyenLieuDropdownDTO> dsNguyenLieu = hangHoaService.getNguyenLieuForDropdown();
        try {
            // Gửi DTO đã chuyển sang JSON
            model.addAttribute("dsNguyenLieuJson", objectMapper.writeValueAsString(dsNguyenLieu));
        } catch (Exception e) {
            model.addAttribute("dsNguyenLieuJson", "[]"); // Gửi mảng rỗng nếu lỗi
        }

        model.addAttribute("currentPage", "admin_thucdon_them");
        return "admin/thucdon/form";
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
            ThucDonFormDTO dto = thucDonService.getThucDonFormDTOById(maThucDon);

            // SỬA LẠI: Lấy DTO thay vì Entity
            List<NguyenLieuDropdownDTO> dsNguyenLieu = hangHoaService.getNguyenLieuForDropdown();

            model.addAttribute("thucDonForm", dto);
            model.addAttribute("currentPage", "admin_thucdon_chinhsua");

            // Gửi cả 2 DTO list sang JSON
            model.addAttribute("dsNguyenLieuJson", objectMapper.writeValueAsString(dsNguyenLieu));
            model.addAttribute("thanhPhanJson", objectMapper.writeValueAsString(dto.getThanhPhan()));

            return "admin/thucdon/form";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/thucdon?error=jsonError";
        }
    }

    /**
     * Xử lý lưu Chỉnh sửa
     * URL: /admin/thucdon/update (POST)
     */
    @PostMapping("/thucdon/update")
    public String updateThucDon(
            @ModelAttribute("thucDonForm") ThucDonFormDTO thucDonForm, // Nhận DTO (đã chứa maThucDon)
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            if (thucDonForm.getMaThucDon() == null) {
                throw new IllegalArgumentException("Thiếu Mã thực đơn khi cập nhật.");
            }

            thucDonService.updateThucDon(thucDonForm);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật món ăn thành công!");
            return "redirect:/admin/thucdon";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Lỗi khi cập nhật món: " + e.getMessage());
            // Gửi lại dữ liệu cũ để form hiển thị
            model.addAttribute("thucDonForm", thucDonForm);
            model.addAttribute("dsNguyenLieu", hangHoaService.getAllHangHoa());
            model.addAttribute("currentPage", "admin_thucdon_chinhsua");
            return "admin/thucdon/form";
        }
    }

}
