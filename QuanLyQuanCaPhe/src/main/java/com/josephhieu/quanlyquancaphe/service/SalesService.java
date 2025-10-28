package com.josephhieu.quanlyquancaphe.service;

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

            // Có thể kiểm tra xem có đặt trước nhưng chưa có hóa đơn không (tùy logic)
            // Optional<ChiTietDatBan> futureBooking = chiTietDatBanRepository.findFutureBookingByBanMaBan(maBan);
            // futureBooking.ifPresent(booking -> details.setReservationInfo(...));
        }

        return details;
    }
}
