package com.josephhieu.quanlyquancaphe.controller;

import com.josephhieu.quanlyquancaphe.dto.TableDetailsDTO;
import com.josephhieu.quanlyquancaphe.entity.Ban;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import com.josephhieu.quanlyquancaphe.service.BanService;
import com.josephhieu.quanlyquancaphe.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
