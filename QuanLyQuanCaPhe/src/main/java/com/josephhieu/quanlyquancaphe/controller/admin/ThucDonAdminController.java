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

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu của Admin
 * liên quan đến nghiệp vụ Quản lý Thực đơn (Menu).
 * Bao gồm Xem, Thêm, Sửa, Xóa, và Tìm kiếm món ăn.
 * Các URL đều có tiền tố /admin.
 *
 * @author Joseph Hieu
 * @version 1.0
 * @since 2025-11-03
 */
@Controller
@RequestMapping("/admin")
public class ThucDonAdminController {

    @Autowired
    private ThucDonService thucDonService;

    @Autowired
    private HangHoaService hangHoaService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Hiển thị trang danh sách tất cả món ăn trong thực đơn.
     * Xử lý URL: GET /admin/thucdon
     *
     * @param model Model để truyền danh sách món ăn (dsThucDon) ra view.
     * @return Tên view template "admin/thucdon/list".
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
     * Hiển thị form để thêm một món ăn mới.
     * Gửi một DTO rỗng (thucDonForm) và danh sách nguyên liệu (dsNguyenLieuJson) ra view.
     * Xử lý URL: GET /admin/thucdon/them
     *
     * @param model Model để truyền dữ liệu ra view.
     * @return Tên view template "admin/thucdon/form".
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
     * Xử lý việc lưu một món ăn mới (bao gồm cả các thành phần).
     * Xử lý URL: POST /admin/thucdon/save
     *
     * @param thucDonForm        Đối tượng DTO được bind từ form (chứa tên, giá, và list thành phần).
     * @param redirectAttributes Dùng để gửi thông báo (success/error) khi chuyển hướng.
     * @param model              Dùng để trả về form nếu có lỗi validation.
     * @return Chuyển hướng về trang danh sách nếu thành công, ngược lại trả về form.
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
     * Hiển thị trang Tìm kiếm và xử lý kết quả tìm kiếm món ăn.
     * Xử lý URL: GET /admin/thucdon/timkiem
     *
     * @param keyword Từ khóa tìm kiếm (lấy từ URL param, không bắt buộc).
     * @param model Model để truyền kết quả (dsKetQua) và từ khóa (keyword) ra view.
     * @return Tên view template "admin/thucdon/search".
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
     * Xử lý nghiệp vụ Xóa món ăn (và các thành phần liên quan).
     * Xử lý URL: GET /admin/thucdon/delete/{id}
     *
     * @param maThucDon Mã UUID của món ăn cần xóa (lấy từ URL path).
     * @param redirectAttributes Dùng để gửi thông báo (success/error) khi chuyển hướng.
     * @return Chuyển hướng về trang danh sách (/admin/thucdon).
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
     * Hiển thị form Chỉnh sửa món ăn với dữ liệu cũ (bao gồm cả thành phần).
     * Xử lý URL: GET /admin/thucdon/edit/{id}
     *
     * @param maThucDon Mã UUID của món ăn (lấy từ URL path).
     * @param model Model để truyền DTO (thucDonForm) và JSON (thanhPhanJson, dsNguyenLieuJson) ra view.
     * @return Tên view template "admin/thucdon/form" (dùng chung).
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
     * Xử lý Cập nhật (lưu) món ăn đã chỉnh sửa.
     * Logic nghiệp vụ: Xóa tất cả thành phần cũ, thêm lại thành phần mới.
     * Xử lý URL: POST /admin/thucdon/update
     *
     * @param thucDonForm        Đối tượng DTO được bind từ form (đã chứa maThucDon).
     * @param redirectAttributes Dùng để gửi thông báo khi chuyển hướng.
     * @param model              Dùng để trả về form nếu có lỗi.
     * @return Chuyển hướng về trang danh sách nếu thành công, ngược lại trả về form.
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
