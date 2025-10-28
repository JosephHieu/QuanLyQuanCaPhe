package com.josephhieu.quanlyquancaphe.controller;

import com.josephhieu.quanlyquancaphe.dto.MergeTablesRequestDTO;
import com.josephhieu.quanlyquancaphe.dto.MoveTableRequestDTO;
import com.josephhieu.quanlyquancaphe.dto.TableDetailsDTO;
import com.josephhieu.quanlyquancaphe.entity.Ban;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.service.BanService;
import com.josephhieu.quanlyquancaphe.service.SalesService;
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

}
