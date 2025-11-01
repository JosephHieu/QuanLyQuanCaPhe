package com.josephhieu.quanlyquancaphe.controller.admin;

import com.josephhieu.quanlyquancaphe.dto.ChiTieuDTO;
import com.josephhieu.quanlyquancaphe.dto.ChiTieuListDTO;
import com.josephhieu.quanlyquancaphe.dto.TongThuChiDTO;
import com.josephhieu.quanlyquancaphe.entity.ChiTieu;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.service.NganSachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat; // Thêm
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate; // Thêm
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/ngansach")
public class NganSachAdminController {

    @Autowired
    private NganSachService nganSachService;

    /**
     * Hiển thị trang Xem thu chi (Quản lý ngân sách)
     * URL: /admin/ngansach
     */
    @GetMapping("")
    public String showNganSachPage(
            // Lấy param từ URL, nếu không có thì dùng ngày hôm nay
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            Model model
    ) {
        // 1. Đặt giá trị mặc định nếu param rỗng
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1); // Ngày đầu tháng
        }
        if (endDate == null) {
            endDate = LocalDate.now(); // Ngày hôm nay
        }

        // 2. Gọi Service để lấy dữ liệu tổng hợp
        TongThuChiDTO dto = nganSachService.getTongHopThuChi(startDate, endDate);

        // 3. Gửi dữ liệu ra view
        model.addAttribute("dto", dto); // Dữ liệu bảng và tổng
        model.addAttribute("startDate", startDate); // Gửi lại ngày đã chọn
        model.addAttribute("endDate", endDate);     // Gửi lại ngày đã chọn
        model.addAttribute("currentPage", "admin_ngansach_list"); // Active sidebar

        return "admin/ngansach/list"; // templates/admin/ngansach/list.html
    }

    /**
     * CẬP NHẬT: Hiển thị form/bảng Thêm chi tiêu
     * URL: /admin/ngansach/themchi
     */
    @GetMapping("/themchi")
    public String showThemChiTieuForm(Model model) {
        // Lấy danh sách chi tiêu cũ để hiển thị
        List<ChiTieu> chiTieuList = nganSachService.getRecentChiTieu();

        // Chuyển đổi Entity sang DTO để gửi ra view
        ChiTieuListDTO dtoWrapper = new ChiTieuListDTO();
        List<ChiTieuDTO> dtos = chiTieuList.stream().map(ct -> {
            ChiTieuDTO dto = new ChiTieuDTO();
            dto.setMaChiTieu(ct.getMaChiTieu());
            dto.setNgayChi(ct.getNgayChi());
            dto.setTenKhoanChi(ct.getTenKhoanChi());
            dto.setSoTien(ct.getSoTien());
            return dto;
        }).collect(Collectors.toList());
        dtoWrapper.setDanhSachChiTieu(dtos);

        model.addAttribute("chiTieuListDTO", dtoWrapper); // Gửi DTO wrapper ra
        model.addAttribute("currentPage", "admin_ngansach_themchi");
        return "admin/ngansach/themchi_form";
    }

    /**
     * CẬP NHẬT: Xử lý lưu nhiều khoản chi (Thêm/Sửa)
     * URL: /admin/ngansach/themchi/save (POST)
     */
    @PostMapping("/themchi/save")
    public String saveChiTieu(
            @ModelAttribute ChiTieuListDTO chiTieuListDTO, // Nhận DTO wrapper
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        String tenDangNhapNhanVien = authentication.getName();

        try {
            nganSachService.saveChiTieuList(chiTieuListDTO.getDanhSachChiTieu(), tenDangNhapNhanVien);

            redirectAttributes.addFlashAttribute("successMessage", "Lưu chi tiêu thành công!");
            return "redirect:/admin/ngansach"; // Về trang xem thu chi

        } catch (IllegalArgumentException | NotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("chiTieuListDTO", chiTieuListDTO); // Gửi lại dữ liệu đã nhập
            model.addAttribute("currentPage", "admin_ngansach_themchi");
            return "admin/ngansach/themchi_form";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống khi lưu chi tiêu.");
            model.addAttribute("chiTieuListDTO", chiTieuListDTO);
            model.addAttribute("currentPage", "admin_ngansach_themchi");
            return "admin/ngansach/themchi_form";
        }
    }
}