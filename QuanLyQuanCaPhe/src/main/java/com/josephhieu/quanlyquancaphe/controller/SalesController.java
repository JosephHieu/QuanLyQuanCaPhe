package com.josephhieu.quanlyquancaphe.controller;

import com.josephhieu.quanlyquancaphe.dto.*;
import com.josephhieu.quanlyquancaphe.entity.Ban;
import com.josephhieu.quanlyquancaphe.entity.ThucDon;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.service.BanService;
import com.josephhieu.quanlyquancaphe.service.SalesService;
import com.josephhieu.quanlyquancaphe.service.ThucDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu cho trang "Quản lý Bán hàng" (POS).
 * Bao gồm việc hiển thị lưới bàn và xử lý tất cả các nghiệp vụ
 * liên quan đến order, bàn (chuyển, gộp, tách, thanh toán, v.v.)
 * thông qua các API endpoint (trả về JSON).
 *
 * @author Joseph Hieu
 * @version 1.0
 * @since 2025-11-03
 */
@Controller
public class SalesController {

    @Autowired
    private BanService banService;

    @Autowired
    private SalesService salesService;

    @Autowired
    private ThucDonService thucDonService;

    /**
     * Hiển thị trang Quản lý Bán hàng chính (lưới bàn).
     * Xử lý URL: GET /sales
     *
     * @param model Model để truyền danh sách bàn (dsBan) ra view.
     * @return Tên view template "sales/view".
     */
    @GetMapping("/sales")
    public String showSalesPage(Model model) {

        List<Ban> dsBan = banService.getAllBan();

        model.addAttribute("dsBan", dsBan);

        model.addAttribute("currentPage", "sales");

        return "sales/view";
    }

    /**
     * API: Lấy thông tin chi tiết của một bàn (món đã gọi, thông tin đặt trước).
     * Được gọi bằng JavaScript (fetch) khi nhấn nút "Xem bàn".
     * Xử lý URL: GET /sales/table/{maBan}
     *
     * @param maBan Mã UUID của bàn cần xem.
     * @return ResponseEntity chứa {@link TableDetailsDTO} (dạng JSON) nếu thành công (200 OK),
     * hoặc 404 Not Found, hoặc 500 Internal Server Error.
     */
    @GetMapping("/sales/table/{maBan}")
    @ResponseBody // Trả về JSON, không phải tên view
    public ResponseEntity<TableDetailsDTO> getTableDetails(@PathVariable String maBan) {
        try {
            TableDetailsDTO details = salesService.getTableDetails(maBan);
            return ResponseEntity.ok(details); // Trả về 200 OK và dữ liệu JSON
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build(); // Trả về 404 Not Found
        } catch (Exception e) {
            // Log lỗi ra console
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // Trả về 500 Internal Server Error
        }
    }

    /**
     * API: Xử lý nghiệp vụ chuyển toàn bộ hóa đơn từ bàn nguồn sang bàn đích.
     * Xử lý URL: POST /sales/move-table
     *
     * @param request DTO chứa sourceTableId và destinationTableId.
     * @return ResponseEntity 200 OK nếu thành công, 400 Bad Request nếu lỗi logic.
     */
    @PostMapping("/sales/move-table") // *** ĐẢM BẢO DÒNG NÀY ĐÚNG ***
    @ResponseBody // Trả về JSON/text
    public ResponseEntity<?> moveTable(@RequestBody MoveTableRequestDTO request) {
        try {
            salesService.moveTable(request.getSourceTableId(), request.getDestinationTableId());
            return ResponseEntity.ok().body("{\"message\": \"Chuyển bàn thành công!\"}");
        } catch (NotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi chuyển bàn.");
        }
    }

    /**
     * API: Xử lý nghiệp vụ gộp nhiều hóa đơn từ các bàn nguồn vào một bàn đích.
     * Xử lý URL: POST /sales/merge-tables
     *
     * @param request DTO chứa danh sách sourceTableIds và destinationTableId.
     * @return ResponseEntity 200 OK nếu thành công, 400 Bad Request nếu lỗi logic.
     */
    @PostMapping("/sales/merge-tables")
    @ResponseBody
    public ResponseEntity<?> mergeTables(@RequestBody MergeTablesRequestDTO request) {
        try {
            salesService.mergeTables(request.getSourceTableIds(), request.getDestinationTableId());
            // Trả về 200 OK
            return ResponseEntity.ok().body("{\"message\": \"Gộp bàn thành công!\"}");
        } catch (NotFoundException | IllegalArgumentException e) {
            // Trả về 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về 500 Internal Server Error
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi gộp bàn.");
        }
    }

    /**
     * API: Xử lý nghiệp vụ tách một số món từ bàn nguồn sang bàn đích (bàn trống).
     * Xử lý URL: POST /sales/split-table
     *
     * @param request DTO chứa sourceTableId, destinationTableId, và danh sách món (items) cần tách.
     * @return ResponseEntity 200 OK nếu thành công, 400 Bad Request nếu lỗi logic.
     */
    @PostMapping("/sales/split-table")
    @ResponseBody
    public ResponseEntity<?> splitTable(@RequestBody SplitTableRequestDTO request) {
        try {
            salesService.splitTable(request.getSourceTableId(), request.getDestinationTableId(), request.getItems());
            // Trả về 200 OK
            return ResponseEntity.ok().body("{\"message\": \"Tách bàn thành công!\"}");
        } catch (NotFoundException | IllegalArgumentException e) {
            // Trả về 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về 500 Internal Server Error
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi tách bàn.");
        }
    }

    /**
     * API: Xử lý nghiệp vụ hủy bàn (xóa hóa đơn, chi tiết, đặt bàn và set bàn về "Trống").
     * Xử lý URL: POST /sales/cancel-order
     *
     * @param request DTO chứa maBan cần hủy.
     * @return ResponseEntity 200 OK nếu thành công, 400 Bad Request nếu lỗi logic.
     */
    @PostMapping("/sales/cancel-order")
    @ResponseBody
    public ResponseEntity<?> cancelOrder(@RequestBody CancelOrderRequestDTO request) {
        try {
            salesService.cancelOrder(request.getMaBan());
            // Trả về 200 OK (có thể không cần body)
            return ResponseEntity.ok().build();
            // Hoặc: return ResponseEntity.ok().body("{\"message\": \"Hủy bàn thành công!\"}");
        } catch (NotFoundException | IllegalArgumentException e) {
            // Trả về 400 Bad Request hoặc 404 Not Found tùy ngữ cảnh
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về 500 Internal Server Error
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi hủy bàn.");
        }
    }

    /**
     * API: Xử lý nghiệp vụ đặt bàn (tạo Hóa đơn 0đ, tạo ChiTietDatBan, set bàn "Đặt trước").
     * Xử lý URL: POST /sales/reserve-table
     *
     * @param request DTO chứa thông tin đặt bàn (maBan, tenKhachHang, ngayGioDat...).
     * @return ResponseEntity 200 OK nếu thành công, 400 Bad Request nếu lỗi logic.
     */
    @PostMapping("/sales/reserve-table")
    @ResponseBody
    public ResponseEntity<?> reserveTable(@RequestBody ReserveTableRequestDTO request) {
        try {
            salesService.reserveTable(request);
            // Trả về 200 OK
            return ResponseEntity.ok().body("{\"message\": \"Đặt bàn thành công!\"}");
        } catch (NotFoundException | IllegalArgumentException e) {
            // Trả về 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về 500 Internal Server Error
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi đặt bàn.");
        }
    }

    /**
     * API: Xử lý Thêm/Sửa/Xóa món ăn cho một hóa đơn đang hoạt động.
     * Được gọi từ modal "Chọn thực đơn".
     * Xử lý URL: POST /sales/update-order
     *
     * @param request DTO chứa maBan và danh sách món ăn (items) mới.
     * @return ResponseEntity 200 OK nếu thành công, 400 Bad Request nếu lỗi logic.
     */
    @PostMapping("/sales/update-order")
    @ResponseBody
    public ResponseEntity<?> updateOrder(@RequestBody AddItemRequestDTO request) { // Dùng lại DTO cũ
        try {
            salesService.updateOrder(request.getMaBan(), request.getItems());
            // Trả về 200 OK
            return ResponseEntity.ok().body("{\"message\": \"Cập nhật đơn hàng thành công!\"}");
        } catch (NotFoundException | IllegalArgumentException e) {
            // Trả về 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về 500 Internal Server Error
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi cập nhật đơn hàng.");
        }
    }

    /**
     * API: Cung cấp toàn bộ danh sách thực đơn (đã sắp xếp).
     * Được gọi bởi JavaScript khi tải trang Bán hàng để chuẩn bị cho modal "Chọn thực đơn".
     * Xử lý URL: GET /menu
     *
     * @return ResponseEntity chứa List<ThucDon> (dạng JSON).
     */
    @GetMapping("/menu")
    @ResponseBody // Trả về JSON
    public ResponseEntity<List<ThucDon>> getMenu() {
        try {
            List<ThucDon> menu = thucDonService.getAllThucDonSorted();
            return ResponseEntity.ok(menu); // Trả về danh sách món
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thực đơn: " + e.getMessage()); // Log lỗi ra console backend
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // Trả về lỗi 500
        }
    }

    /**
     * API: Xử lý nghiệp vụ thanh toán (đánh dấu HoaDon.TrangThai = true, cập nhật Ban.TinhTrang).
     * Xử lý URL: POST /sales/process-payment
     *
     * @param request DTO chứa maBan và cờ resetTable.
     * @return ResponseEntity 200 OK nếu thành công, 400 Bad Request nếu lỗi logic.
     */
    @PostMapping("/sales/process-payment")
    @ResponseBody
    public ResponseEntity<?> processPayment(@RequestBody ProcessPaymentRequestDTO request) {
        try {
            salesService.processPayment(request.getMaBan(), request.isResetTable());
            return ResponseEntity.ok().body("{\"message\": \"Thanh toán thành công!\"}");

            // --- SỬA LẠI CÁC KHỐI CATCH ---

            // 1. Bắt các lỗi cụ thể (NotFound, IllegalArgument) trước
            //    Vì xử lý giống nhau (trả về 400 Bad Request), có thể gộp chúng lại
        } catch (NotFoundException | IllegalArgumentException e) {
            System.err.println("Validation Error during payment: " + e.getMessage()); // Log lỗi cụ thể hơn
            return ResponseEntity.badRequest().body(e.getMessage()); // Trả về lỗi 400

            // 2. Bắt các lỗi RuntimeException khác (không mong đợi)
        } catch (RuntimeException e) { // Bắt RuntimeException riêng
            System.err.println("Unexpected Runtime Error during payment: " + e.getMessage());
            e.printStackTrace(); // In stack trace để debug
            return ResponseEntity.internalServerError().body("Lỗi hệ thống không mong đợi khi thanh toán."); // Trả về 500

            // 3. Bắt lỗi Exception chung (cho các lỗi khác như IOException nếu có)
        } catch (Exception e) {
            System.err.println("General Error during payment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi thanh toán."); // Trả về 500
        }
    }
}
