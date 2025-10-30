package com.josephhieu.quanlyquancaphe.controller.admin;

import com.josephhieu.quanlyquancaphe.entity.HangHoa;
import com.josephhieu.quanlyquancaphe.exception.InsufficientStockException;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.service.DonViTinhService;
import com.josephhieu.quanlyquancaphe.service.HangHoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class KhoHangAdminController {

    @Autowired
    private HangHoaService hangHoaService;

    @Autowired
    private DonViTinhService donViTinhService;

    /**
     * Hiển thị trang Danh sách Hàng hóa (Tồn kho)
     * URL: /admin/khohang
     */
    @GetMapping("/khohang")
    public String showHangHoaList(Model model) {
        // Lấy danh sách hàng hóa tồn kho
        List<HangHoa> dsHangHoa = hangHoaService.getAllHangHoa();

        // Gửi ra view
        model.addAttribute("dsHangHoa", dsHangHoa);

        // Gửi tín hiệu active cho sidebar
        model.addAttribute("currentPage", "admin_khohang_list"); // Đặt tên phù hợp

        // Trả về file HTML
        return "admin/khohang/list"; // templates/admin/khohang/list.html
    }

    /**
     * PHƯƠNG THỨC MỚI: Hiển thị form Nhập hàng hóa
     * URL: /admin/khohang/nhap
     */
    @GetMapping("/khohang/nhap")
    public String showNhapHangHoaForm(Model model) {

        model.addAttribute("hangHoa", new HangHoa());

        // Gửi danh sách đơn vị tính ra form
        model.addAttribute("dsDonViTinh", donViTinhService.getAllDonViTinh());
        // Gửi ngày hiện tại làm mặc định
        model.addAttribute("ngayNhapDefault", LocalDate.now());
        // Gửi tín hiệu active sidebar
        model.addAttribute("currentPage", "admin_khohang_nhap");
        // Trả về file HTML form
        return "admin/khohang/nhap_form"; // templates/admin/khohang/nhap_form.html
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý lưu Nhập hàng hóa
     * URL: /admin/khohang/nhap/save (POST)
     */
    @PostMapping("/khohang/nhap/save")
    public String saveNhapHangHoa(
            @RequestParam("tenHangHoa") String tenHangHoa,
            @RequestParam("soLuong") int soLuong,
            @RequestParam("maDonViTinh") String maDonViTinh,
            @RequestParam("donGia") BigDecimal donGia,
            @RequestParam("ngayNhap") LocalDate ngayNhap,
            Authentication authentication, // Lấy nhân viên đang login
            RedirectAttributes redirectAttributes, // Gửi thông báo
            Model model // Để trả về form nếu lỗi
    ) {
        // Lấy tên đăng nhập nhân viên
        String tenDangNhapNhanVien = authentication.getName();

        try {
            // Validate dữ liệu cơ bản
            if (tenHangHoa == null || tenHangHoa.trim().isEmpty() ||
                    maDonViTinh == null || maDonViTinh.isEmpty() ||
                    donGia == null || donGia.compareTo(BigDecimal.ZERO) < 0 ||
                    ngayNhap == null) {
                throw new IllegalArgumentException("Vui lòng điền đầy đủ thông tin hợp lệ.");
            }

            // Gọi service để xử lý nhập kho
            hangHoaService.nhapHangHoa(tenHangHoa, soLuong, maDonViTinh, donGia, ngayNhap, tenDangNhapNhanVien);

            // Gửi thông báo thành công và chuyển hướng
            redirectAttributes.addFlashAttribute("successMessage", "Nhập hàng hóa thành công!");
            return "redirect:/admin/khohang"; // Về trang danh sách kho

        } catch (IllegalArgumentException | NotFoundException e) {
            // Lỗi validation hoặc không tìm thấy Đơn vị tính
            model.addAttribute("errorMessage", e.getMessage());
            // Gửi lại dữ liệu đã nhập và danh sách đơn vị
            model.addAttribute("tenHangHoa", tenHangHoa);
            model.addAttribute("soLuong", soLuong);
            model.addAttribute("maDonViTinhSelected", maDonViTinh);
            model.addAttribute("donGia", donGia);
            model.addAttribute("ngayNhap", ngayNhap);
            model.addAttribute("dsDonViTinh", donViTinhService.getAllDonViTinh());
            model.addAttribute("currentPage", "admin_khohang_nhap");
            return "admin/khohang/nhap_form"; // Trả về form
        } catch (Exception e) {
            // Lỗi hệ thống khác
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống khi nhập hàng.");
            // Gửi lại dữ liệu
            model.addAttribute("tenHangHoa", tenHangHoa);
            model.addAttribute("soLuong", soLuong);
            model.addAttribute("maDonViTinhSelected", maDonViTinh);
            model.addAttribute("donGia", donGia);
            model.addAttribute("ngayNhap", ngayNhap);
            model.addAttribute("dsDonViTinh", donViTinhService.getAllDonViTinh());
            model.addAttribute("currentPage", "admin_khohang_nhap");
            return "admin/khohang/nhap_form";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Hiển thị form Xuất hàng hóa
     * URL: /admin/khohang/xuat
     */
    @GetMapping("/khohang/xuat")
    public String showXuatHangHoaForm(Model model) {
        // Gửi danh sách hàng hóa hiện có (để làm dropdown)
        // Chỉ lấy hàng còn tồn kho? (Tùy chọn)
        List<HangHoa> dsHangHoaTonKho = hangHoaService.getAllHangHoa().stream()
                .filter(hh -> hh.getSoLuong() > 0)
                .toList();
        model.addAttribute("dsHangHoa", dsHangHoaTonKho);
        // Gửi ngày hiện tại làm mặc định
        model.addAttribute("ngayXuatDefault", LocalDate.now());
        // Gửi tín hiệu active sidebar
        model.addAttribute("currentPage", "admin_khohang_xuat");
        // Trả về file HTML form
        return "admin/khohang/xuat_form"; // templates/admin/khohang/xuat_form.html
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý lưu Xuất hàng hóa
     * URL: /admin/khohang/xuat/save (POST)
     */
    @PostMapping("/khohang/xuat/save")
    public String saveXuatHangHoa(
            @RequestParam("maHangHoa") String maHangHoa,
            @RequestParam("soLuong") int soLuong,
            @RequestParam("ngayXuat") LocalDate ngayXuat,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        String tenDangNhapNhanVien = authentication.getName();

        try {
            // Validate cơ bản
            if (maHangHoa == null || maHangHoa.isEmpty() || ngayXuat == null) {
                throw new IllegalArgumentException("Vui lòng chọn hàng hóa và ngày xuất.");
            }

            // Gọi service để xử lý xuất kho
            hangHoaService.xuatHangHoa(maHangHoa, soLuong, ngayXuat, tenDangNhapNhanVien);

            redirectAttributes.addFlashAttribute("successMessage", "Xuất hàng hóa thành công!");
            return "redirect:/admin/khohang"; // Về trang danh sách kho

        } catch (IllegalArgumentException | NotFoundException | InsufficientStockException e) {
            // Lỗi validation, không tìm thấy hàng, hoặc không đủ tồn kho
            model.addAttribute("errorMessage", e.getMessage());
            // Gửi lại danh sách hàng và ngày mặc định
            List<HangHoa> dsHangHoaTonKho = hangHoaService.getAllHangHoa().stream().filter(hh -> hh.getSoLuong() > 0).toList();
            model.addAttribute("dsHangHoa", dsHangHoaTonKho);
            model.addAttribute("ngayXuatDefault", LocalDate.now()); // Hoặc giữ lại ngày đã nhập: model.addAttribute("ngayXuat", ngayXuat);
            model.addAttribute("maHangHoaSelected", maHangHoa); // Giữ lại hàng đã chọn
            model.addAttribute("soLuong", soLuong); // Giữ lại số lượng đã nhập
            model.addAttribute("currentPage", "admin_khohang_xuat");
            return "admin/khohang/xuat_form"; // Trả về form
        } catch (Exception e) {
            // Lỗi hệ thống khác
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống khi xuất hàng.");
            List<HangHoa> dsHangHoaTonKho = hangHoaService.getAllHangHoa().stream().filter(hh -> hh.getSoLuong() > 0).toList();
            model.addAttribute("dsHangHoa", dsHangHoaTonKho);
            model.addAttribute("ngayXuatDefault", LocalDate.now());
            model.addAttribute("maHangHoaSelected", maHangHoa);
            model.addAttribute("soLuong", soLuong);
            model.addAttribute("currentPage", "admin_khohang_xuat");
            return "admin/khohang/xuat_form";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Hiển thị form Chỉnh sửa hàng hóa
     * URL: /admin/khohang/edit/{id}
     */
    /**
     * Hiển thị form Chỉnh sửa hàng hóa
     * URL: /admin/khohang/edit/{id}
     */
    @GetMapping("/khohang/edit/{id}")
    public String showEditHangHoaForm(@PathVariable("id") String maHangHoa, Model model) {
        try {
            HangHoa hangHoa = hangHoaService.getHangHoaById(maHangHoa)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa: " + maHangHoa));

            // *** QUAN TRỌNG: Đảm bảo tên attribute là "hangHoa" ***
            model.addAttribute("hangHoa", hangHoa);

            model.addAttribute("dsDonViTinh", donViTinhService.getAllDonViTinh());
            model.addAttribute("currentPage", "admin_khohang_chinhsua");

            // *** QUAN TRỌNG: Đảm bảo trả về đúng file view cho Sửa ***
            return "admin/khohang/form"; // File này dùng cho Sửa

        } catch (NotFoundException e) {
            return "redirect:/admin/khohang?error=notFound";
        }
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý CẬP NHẬT hàng hóa
     * URL: /admin/khohang/update (POST)
     */
    @PostMapping("/khohang/update")
    public String updateHangHoa(
            @ModelAttribute("hangHoa") HangHoa hangHoaFromForm, // Nhận ID và các trường khác
            @RequestParam("maDonViTinh") String maDonViTinh, // Nhận mã đơn vị tính riêng
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            // Thêm validation tương tự như khi nhập hàng (tên, đơn vị, giá, số lượng >= 0)
            if (hangHoaFromForm.getTenHangHoa() == null || hangHoaFromForm.getTenHangHoa().trim().isEmpty() ||
                    maDonViTinh == null || maDonViTinh.isEmpty() ||
                    hangHoaFromForm.getSoLuong() < 0 || // Cho phép tồn kho = 0
                    hangHoaFromForm.getDonGia() == null || hangHoaFromForm.getDonGia().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Vui lòng điền đầy đủ thông tin hợp lệ.");
            }

            // Gọi service update
            hangHoaService.updateHangHoa(hangHoaFromForm.getMaHangHoa(), hangHoaFromForm, maDonViTinh);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hàng hóa thành công!");
            return "redirect:/admin/khohang";

        } catch (IllegalArgumentException | NotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            // Gửi lại dữ liệu đã sửa và danh sách đơn vị
            model.addAttribute("hangHoa", hangHoaFromForm);
            model.addAttribute("dsDonViTinh", donViTinhService.getAllDonViTinh());
            model.addAttribute("currentPage", "admin_khohang_chinhsua");
            return "admin/khohang/form";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật hàng hóa.");
            model.addAttribute("hangHoa", hangHoaFromForm);
            model.addAttribute("dsDonViTinh", donViTinhService.getAllDonViTinh());
            model.addAttribute("currentPage", "admin_khohang_chinhsua");
            return "admin/khohang/form";
        }
    }

    /**
     * HIỂN THỊ TRANG TÌM KIẾM & XỬ LÝ TÌM KIẾM
     * URL: /admin/khohang/timkiem
     */
    @GetMapping("/khohang/timkiem")
    public String showHangHoaSearchPage(
            @RequestParam(value = "keyword", required = false) String keyword, // Nhận từ khóa (không bắt buộc)
            Model model
    ) {
        // Gọi service để tìm kiếm
        List<HangHoa> dsKetQua = hangHoaService.searchHangHoa(keyword);

        // Gửi kết quả tìm kiếm ra view
        model.addAttribute("dsKetQua", dsKetQua);
        // Gửi lại từ khóa để hiển thị trên ô input
        model.addAttribute("keyword", keyword);

        // Đặt currentPage cho sidebar
        model.addAttribute("currentPage", "admin_khohang_timkiem"); // Hoặc _chinhsua, _xoa nếu muốn

        // Trả về file HTML mới
        return "admin/khohang/search";
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý xóa hàng hóa
     * URL: /admin/khohang/delete/{id}
     */
    @GetMapping("/khohang/delete/{id}")
    public String deleteHangHoa(@PathVariable("id") String maHangHoa, RedirectAttributes redirectAttributes) {
        try {
            hangHoaService.deleteHangHoa(maHangHoa);
            // Gửi thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa hàng hóa thành công!");
        } catch (NotFoundException | DataIntegrityViolationException e) {
            // Gửi thông báo lỗi
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn khi xóa.");
        }

        // Luôn chuyển hướng về trang danh sách kho
        return "redirect:/admin/khohang";
    }
}
