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

@Controller
public class SalesController {

    @Autowired
    private BanService banService;

    @Autowired
    private SalesService salesService;

    @Autowired
    private ThucDonService thucDonService;

    @GetMapping("/sales")
    public String showSalesPage(Model model) {

        List<Ban> dsBan = banService.getAllBan();

        model.addAttribute("dsBan", dsBan);

        model.addAttribute("currentPage", "sales");

        return "sales/view";
    }

    // Lấy thông tin chi tiết một bàn
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
     * Endpoint xử lý yêu cầu chuyển bàn
     * URL: /sales/move-table (POST)
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
     * Endpoint MỚI: Xử lý yêu cầu gộp bàn
     * URL: /sales/merge-tables (POST)
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
     * Endpoint MỚI: Xử lý yêu cầu tách bàn
     * URL: /sales/split-table (POST)
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
     * Endpoint MỚI: Xử lý yêu cầu hủy bàn
     * URL: /sales/cancel-order (POST)
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
     * Endpoint MỚI: Xử lý yêu cầu đặt bàn
     * URL: /sales/reserve-table (POST)
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
     * Endpoint MỚI: Xử lý cập nhật đơn hàng cho bàn
     * URL: /sales/update-order (POST)
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
     * Endpoint MỚI: Cung cấp danh sách món ăn (Thực đơn)
     * URL: /menu (GET)
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
     * Endpoint MỚI: Xử lý yêu cầu thanh toán
     * URL: /sales/process-payment (POST)
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

    // (Optional: Endpoint để in hóa đơn)
     /*
     @GetMapping("/sales/bill/{maBan}/print")
     public String printBillPage(@PathVariable String maBan, Model model) {
         // Lấy thông tin hóa đơn đã thanh toán (hoặc chưa) của bàn
         // Trả về một trang HTML được thiết kế để in
         return "sales/bill_print";
     }
     */
}
