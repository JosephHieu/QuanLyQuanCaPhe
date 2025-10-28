package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.id.ChiTietDatBanId;
import org.springframework.stereotype.Service;

import com.josephhieu.quanlyquancaphe.dto.OrderItemDTO;
import com.josephhieu.quanlyquancaphe.dto.ReservationInfoDTO;
import com.josephhieu.quanlyquancaphe.dto.TableDetailsDTO;
import com.josephhieu.quanlyquancaphe.entity.*;
import com.josephhieu.quanlyquancaphe.repository.*;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalesService {

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private ChiTietDatBanRepository chiTietDatBanRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Transactional
    public void moveTable(String sourceTableId, String destinationTableId) {
        // 1. Kiểm tra bàn nguồn
        Ban sourceTable = banRepository.findById(sourceTableId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn nguồn: " + sourceTableId));
        if ("Trống".equalsIgnoreCase(sourceTable.getTinhTrang())) {
            throw new IllegalArgumentException("Không thể chuyển từ bàn trống.");
        }

        // 2. Kiểm tra bàn đích
        Ban destinationTable = banRepository.findById(destinationTableId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn đích: " + destinationTableId));
        if (!"Trống".equalsIgnoreCase(destinationTable.getTinhTrang())) {
            throw new IllegalArgumentException("Bàn đích phải là bàn trống.");
        }

        // 3. Tìm ChiTietDatBan (hóa đơn) đang hoạt động của bàn nguồn
        // Giả định: HoaDon.TrangThai = false (0) là chưa thanh toán
        ChiTietDatBan activeBooking = chiTietDatBanRepository.findByBanMaBanAndHoaDonTrangThai(sourceTableId, false)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn/đặt bàn đang hoạt động cho bàn nguồn."));

        // 4. Lấy thông tin cần thiết từ bản ghi cũ
        HoaDon hoaDon = activeBooking.getHoaDon();
        NhanVien nhanVien = activeBooking.getNhanVien();

        // 5. Tạo bản ghi ChiTietDatBan mới cho bàn đích
        ChiTietDatBanId newBookingId = new ChiTietDatBanId();
        newBookingId.setMaBan(destinationTableId); // Dùng ID Bàn mới
        newBookingId.setMaNhanVien(nhanVien.getMaNhanVien());
        newBookingId.setMaHoaDon(hoaDon.getMaHoaDon());

        ChiTietDatBan newBooking = new ChiTietDatBan();
        newBooking.setId(newBookingId);
        newBooking.setBan(destinationTable); // Liên kết object Bàn mới
        newBooking.setNhanVien(nhanVien);     // Giữ nguyên Nhân viên
        newBooking.setHoaDon(hoaDon);         // Giữ nguyên Hóa đơn
        newBooking.setTenKhachHang(activeBooking.getTenKhachHang());
        newBooking.setSdtKhachHang(activeBooking.getSdtKhachHang());
        newBooking.setNgayGioDat(activeBooking.getNgayGioDat());

        // 6. Xóa bản ghi ChiTietDatBan cũ
        chiTietDatBanRepository.delete(activeBooking);

        // 7. Lưu bản ghi ChiTietDatBan mới
        chiTietDatBanRepository.save(newBooking);

        // 8. Cập nhật trạng thái 2 bàn
        String originalSourceStatus = sourceTable.getTinhTrang(); // Lấy trạng thái bàn nguồn
        sourceTable.setTinhTrang("Trống");                         // Bàn nguồn thành Trống
        destinationTable.setTinhTrang(originalSourceStatus);       // Bàn đích nhận trạng thái bàn nguồn

        banRepository.save(sourceTable);
        banRepository.save(destinationTable);

        // (Nếu có các logic khác cần cập nhật, ví dụ: log lịch sử chuyển bàn...)
        System.out.println("Đã chuyển thành công từ bàn " + sourceTable.getTenBan() + " sang bàn " + destinationTable.getTenBan());
    }

    @Transactional(readOnly = true)
    public TableDetailsDTO getTableDetails(String maBan) {
        Ban ban = banRepository.findById(maBan)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn: " + maBan));

        TableDetailsDTO details = new TableDetailsDTO();
        details.setMaBan(ban.getMaBan());
        details.setTenBan(ban.getTenBan());

        // Tìm ChiTietDatBan liên quan đến bàn này VÀ có HoaDon đang hoạt động (chưa thanh toán)
        // Giả định: HoaDon.TrangThai = false (0) là chưa thanh toán
        Optional<ChiTietDatBan> activeBookingOpt = chiTietDatBanRepository.findByBanMaBanAndHoaDonTrangThai(maBan, false);

        if (activeBookingOpt.isPresent()) {
            ChiTietDatBan activeBooking = activeBookingOpt.get();
            HoaDon activeHoaDon = activeBooking.getHoaDon();

            // Lấy thông tin đặt trước
            details.setReservationInfo(new ReservationInfoDTO(
                    activeBooking.getTenKhachHang(),
                    activeBooking.getNgayGioDat()
            ));

            // Lấy danh sách món đã gọi từ hóa đơn đang hoạt động
            List<ChiTietHoaDon> chiTietList = chiTietHoaDonRepository.findByHoaDonMaHoaDon(activeHoaDon.getMaHoaDon());
            details.setOrderedItems(
                    chiTietList.stream()
                            .map(ct -> new OrderItemDTO(ct.getThucDon().getTenMon(), ct.getSoLuong()))
                            .collect(Collectors.toList())
            );

        } else {
            // Nếu không có hóa đơn đang hoạt động (bàn trống hoặc chỉ đặt trước chưa gọi món)
            details.setOrderedItems(Collections.emptyList());
        }

        return details;
    }
}
