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

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu của Admin
 * liên quan đến nghiệp vụ Quản lý Ngân sách.
 * Bao gồm "Xem thu chi" (Báo cáo) và "Thêm chi tiêu" (CRUD).
 * Các URL đều có tiền tố /admin/ngansach.
 *
 * @author Joseph Hieu
 * @version 1.0
 * @since 2025-11-03
 */
@Controller
@RequestMapping("/admin/ngansach")
public class NganSachAdminController {

    @Autowired
    private NganSachService nganSachService;

    /**
     * Hiển thị trang "Xem thu chi" (trang chính của Quản lý ngân sách).
     * Tổng hợp Thu (từ Hóa đơn đã thanh toán) và Chi (từ Chi tiêu)
     * trong một khoảng ngày.
     * Xử lý URL: GET /admin/ngansach
     *
     * @param startDate Ngày bắt đầu (lấy từ URL param, không bắt buộc).
     * @param endDate   Ngày kết thúc (lấy từ URL param, không bắt buộc).
     * @param model     Model để truyền dữ liệu DTO tổng hợp ra view.
     * @return Tên view template "admin/ngansach/list".
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
     * Hiển thị form/bảng động "Thêm chi tiêu".
     * Tải danh sách các khoản chi gần đây để hiển thị sẵn trong bảng.
     * Xử lý URL: GET /admin/ngansach/themchi
     *
     * @param model Model để truyền danh sách chi tiêu (ChiTieuListDTO) ra view.
     * @return Tên view template "admin/ngansach/themchi_form".
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
     * Xử lý lưu một danh sách các khoản chi tiêu (Thêm mới và Cập nhật).
     * Nhận một DTO chứa danh sách các khoản chi từ form động.
     * Xử lý URL: POST /admin/ngansach/themchi/save
     *
     * @param chiTieuListDTO     Đối tượng DTO (wrapper) được bind từ form (th:object).
     * @param authentication     Để lấy thông tin nhân viên (tài khoản) đang thực hiện.
     * @param redirectAttributes Dùng để gửi thông báo (success/error) khi chuyển hướng.
     * @param model              Dùng để trả về form nếu có lỗi.
     * @return Chuyển hướng về trang "Xem thu chi" nếu thành công, ngược lại trả về form.
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