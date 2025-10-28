package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.dto.*;
import com.josephhieu.quanlyquancaphe.entity.id.ChiTietDatBanId;
import com.josephhieu.quanlyquancaphe.entity.id.ChiTietHoaDonId;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.josephhieu.quanlyquancaphe.entity.*;
import com.josephhieu.quanlyquancaphe.repository.*;
import com.josephhieu.quanlyquancaphe.dto.AddItemRequestDTO;
import com.josephhieu.quanlyquancaphe.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Function;

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

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private ThucDonRepository thucDonRepository;

    /**
     * PHƯƠNG THỨC MỚI: Xử lý thanh toán
     */
    @Transactional
    public void processPayment(String maBan, boolean resetTable) {
        // 1. Tìm bàn và hóa đơn hoạt động
        Ban ban = banRepository.findById(maBan)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn: " + maBan));
        ChiTietDatBan booking = chiTietDatBanRepository.findByBanMaBanAndHoaDonTrangThai(maBan, false)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn/đặt bàn đang hoạt động cho bàn " + ban.getTenBan()));
        HoaDon hoaDon = booking.getHoaDon();

        // 2. Đánh dấu hóa đơn đã thanh toán
        hoaDon.setTrangThai(true); // true = đã thanh toán
        // Có thể lưu thêm thời gian thanh toán nếu cần
//         hoaDon.setThoiGianThanhToan(LocalDateTime.now());
        hoaDonRepository.save(hoaDon);

        // 3. Cập nhật trạng thái bàn nếu được yêu cầu
        if (resetTable) {
            ban.setTinhTrang("Trống");
            banRepository.save(ban);
//             Xóa luôn ChiTietDatBan vì bàn đã trống? (Tùy logic)
             chiTietDatBanRepository.delete(booking);
        }
        // Nếu không reset, bàn vẫn giữ trạng thái "Có khách" nhưng hóa đơn đã thanh toán.
        // Cần có logic dọn bàn riêng sau đó.

        System.out.println("Đã thanh toán thành công cho bàn " + ban.getTenBan());
    }

    /**
     * PHƯƠNG THỨC MỚI: Cập nhật toàn bộ đơn hàng cho bàn
     */
    @Transactional
    public void updateOrder(String maBan, List<AddItemRequestDTO.ItemToAddDTO> updatedItemsDto) {
        // 1. Tìm bàn và hóa đơn hoạt động
        Ban ban = banRepository.findById(maBan)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn: " + maBan));
        ChiTietDatBan booking = chiTietDatBanRepository.findByBanMaBanAndHoaDonTrangThai(maBan, false)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt bàn/hóa đơn hoạt động cho bàn này.")); // Cần logic tạo mới nếu bàn trống
        HoaDon hoaDon = booking.getHoaDon();

        // 2. Lấy danh sách ChiTietHoaDon HIỆN TẠI của hóa đơn
        List<ChiTietHoaDon> existingDetails = chiTietHoaDonRepository.findByHoaDonMaHoaDon(hoaDon.getMaHoaDon());
        // Chuyển thành Map để dễ truy cập: Key = MaThucDon, Value = ChiTietHoaDon
        Map<String, ChiTietHoaDon> existingDetailsMap = existingDetails.stream()
                .collect(Collectors.toMap(detail -> detail.getId().getMaThucDon(), Function.identity()));

        List<ChiTietHoaDon> detailsToSave = new ArrayList<>(); // Lưu các chi tiết cần save/update
        List<ChiTietHoaDon> detailsToDelete = new ArrayList<>(); // Lưu các chi tiết cần delete
        BigDecimal newTotalAmount = BigDecimal.ZERO; // Tính lại tổng tiền từ đầu

        // 3. Xử lý danh sách món MỚI từ request
        for (AddItemRequestDTO.ItemToAddDTO newItemDto : updatedItemsDto) {
            String maThucDon = newItemDto.getMaThucDon();
            int newQuantity = newItemDto.getSoLuong();

            if (newQuantity < 0) continue; // Bỏ qua số lượng âm

            ThucDon thucDon = thucDonRepository.findById(maThucDon)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy món ăn: " + maThucDon));
            BigDecimal giaBan = thucDon.getGiaTienHienTai();

            // Kiểm tra xem món này đã có trong hóa đơn cũ chưa (dùng Map)
            ChiTietHoaDon existingDetail = existingDetailsMap.get(maThucDon);

            if (existingDetail != null) {
                // Món đã tồn tại
                if (newQuantity > 0) {
                    // Cập nhật số lượng và thành tiền
                    existingDetail.setSoLuong(newQuantity);
                    existingDetail.setThanhTien(giaBan.multiply(BigDecimal.valueOf(newQuantity)));
                    detailsToSave.add(existingDetail); // Đánh dấu cần update
                    newTotalAmount = newTotalAmount.add(existingDetail.getThanhTien()); // Cộng vào tổng mới
                } else {
                    // Số lượng mới là 0 -> Đánh dấu cần xóa
                    detailsToDelete.add(existingDetail);
                }
                // Xóa khỏi map để lát nữa biết món nào cần xóa hẳn
                existingDetailsMap.remove(maThucDon);
            } else {
                // Món mới hoàn toàn (chưa có trong hóa đơn cũ)
                if (newQuantity > 0) {
                    ChiTietHoaDon newDetail = new ChiTietHoaDon();
                    ChiTietHoaDonId newId = new ChiTietHoaDonId();
                    newId.setMaHoaDon(hoaDon.getMaHoaDon());
                    newId.setMaThucDon(maThucDon);

                    newDetail.setId(newId);
                    newDetail.setHoaDon(hoaDon);
                    newDetail.setThucDon(thucDon);
                    newDetail.setSoLuong(newQuantity);
                    newDetail.setGiaTaiThoiDiemBan(giaBan);
                    newDetail.setThanhTien(giaBan.multiply(BigDecimal.valueOf(newQuantity)));
                    detailsToSave.add(newDetail); // Đánh dấu cần add
                    newTotalAmount = newTotalAmount.add(newDetail.getThanhTien()); // Cộng vào tổng mới
                }
                // Nếu newQuantity = 0 thì không làm gì cả
            }
        } // Kết thúc vòng lặp món mới

        // 4. Những món còn lại trong existingDetailsMap là những món bị xóa (không có trong list mới)
        detailsToDelete.addAll(existingDetailsMap.values());

        // 5. Thực hiện lưu và xóa
        if (!detailsToSave.isEmpty()) {
            chiTietHoaDonRepository.saveAll(detailsToSave);
        }
        if (!detailsToDelete.isEmpty()) {
            chiTietHoaDonRepository.deleteAll(detailsToDelete);
        }

        // 6. Cập nhật tổng tiền hóa đơn
        hoaDon.setTongTien(newTotalAmount);
        hoaDonRepository.save(hoaDon);

        // 7. Cập nhật trạng thái bàn (nếu cần)
        boolean hasItemsNow = !detailsToSave.isEmpty() || !remainingItemsAfterDelete(detailsToSave, detailsToDelete, existingDetails);

        if ("Trống".equalsIgnoreCase(ban.getTinhTrang()) && hasItemsNow) {
            ban.setTinhTrang("Có khách"); // Chuyển thành có khách nếu trước đó trống và giờ có món
            banRepository.save(ban);
        } else if (!"Trống".equalsIgnoreCase(ban.getTinhTrang()) && !hasItemsNow) {
            // Nếu bàn đang có khách/đặt trước mà giờ hết món -> Chuyển thành trống? Hay giữ đặt trước?
            // Tùy logic: Ở đây chuyển về Trống nếu hết món
            ban.setTinhTrang("Trống");
            banRepository.save(ban);
            // Có thể cần xóa cả ChiTietDatBan nếu về trống?
            // chiTietDatBanRepository.delete(booking);
        } else if ("Đặt trước".equalsIgnoreCase(ban.getTinhTrang()) && hasItemsNow) {
            // Nếu là bàn đặt trước và bắt đầu gọi món -> chuyển thành Có khách
            ban.setTinhTrang("Có khách");
            banRepository.save(ban);
        }


        System.out.println("Đã cập nhật đơn hàng cho bàn " + ban.getTenBan());
    }

    // Hàm helper để kiểm tra xem còn món nào sau khi xóa không
    private boolean remainingItemsAfterDelete(List<ChiTietHoaDon> toSave, List<ChiTietHoaDon> toDelete, List<ChiTietHoaDon> existing) {
        Map<String, ChiTietHoaDon> finalItems = existing.stream()
                .filter(d -> !toDelete.contains(d)) // Loại bỏ những cái bị xóa
                .collect(Collectors.toMap(d -> d.getId().getMaThucDon(), Function.identity()));
        // Thêm hoặc cập nhật những cái được save
        toSave.forEach(d -> finalItems.put(d.getId().getMaThucDon(), d));
        // Kiểm tra xem có món nào số lượng > 0 không
        return finalItems.values().stream().anyMatch(d -> d.getSoLuong() > 0);
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý đặt bàn
     */
    @Transactional
    public void reserveTable(ReserveTableRequestDTO request) {
        // 1. Validation bàn
        Ban ban = banRepository.findById(request.getMaBan())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn: " + request.getMaBan()));
        if (!"Trống".equalsIgnoreCase(ban.getTinhTrang())) {
            throw new IllegalArgumentException("Chỉ có thể đặt bàn trống.");
        }
        if (request.getTenKhachHang() == null || request.getTenKhachHang().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng là bắt buộc.");
        }
        if (request.getNgayGioDat() == null) {
            throw new IllegalArgumentException("Ngày giờ đặt là bắt buộc.");
        }
        // Có thể thêm validation thời gian đặt phải trong tương lai

        // 2. Lấy thông tin nhân viên đang đăng nhập
        // Cách này lấy UserDetails, cần tìm NhanVien tương ứng
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String tenDangNhapNhanVien;
        if (principal instanceof UserDetails) {
            tenDangNhapNhanVien = ((UserDetails) principal).getUsername();
        } else {
            tenDangNhapNhanVien = principal.toString(); // Hoặc xử lý khác nếu không phải UserDetails
        }
        NhanVien nhanVien = nhanVienRepository.findByTaiKhoan_TenDangNhap(tenDangNhapNhanVien)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin nhân viên đang đăng nhập."));


        // 3. Tạo Hóa đơn MỚI (chưa có món, chưa thanh toán)
        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayGioTao(LocalDateTime.now()); // Thời gian tạo hóa đơn
        hoaDon.setTrangThai(false); // Chưa thanh toán
        hoaDon.setTongTien(BigDecimal.ZERO); // Tổng tiền ban đầu là 0
        hoaDon = hoaDonRepository.save(hoaDon);

        // 4. Tạo ChiTietDatBan MỚI
        ChiTietDatBanId bookingId = new ChiTietDatBanId();
        bookingId.setMaBan(ban.getMaBan());
        bookingId.setMaNhanVien(nhanVien.getMaNhanVien());
        bookingId.setMaHoaDon(hoaDon.getMaHoaDon());

        ChiTietDatBan booking = new ChiTietDatBan();
        booking.setId(bookingId);
        booking.setBan(ban);
        booking.setNhanVien(nhanVien);
        booking.setHoaDon(hoaDon);
        booking.setTenKhachHang(request.getTenKhachHang());
        booking.setSdtKhachHang(request.getSdtKhachHang());
        booking.setNgayGioDat(request.getNgayGioDat()); // Thời gian khách hẹn đến
        chiTietDatBanRepository.save(booking);

        // 5. Cập nhật trạng thái bàn thành "Đặt trước"
        ban.setTinhTrang("Đặt trước");
        banRepository.save(ban);

        System.out.println("Đã đặt thành công bàn " + ban.getTenBan() + " cho " + request.getTenKhachHang());
    }

    /**
     * PHƯƠNG THỨC MỚI: Xử lý hủy bàn/đơn hàng
     */
    @Transactional
    public void cancelOrder(String maBan) {
        // 1. Tìm bàn
        Ban ban = banRepository.findById(maBan)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn: " + maBan));
        if ("Trống".equalsIgnoreCase(ban.getTinhTrang())) {
            throw new IllegalArgumentException("Bàn đang trống, không thể hủy.");
        }

        // 2. Tìm ChiTietDatBan và HoaDon đang hoạt động
        ChiTietDatBan activeBooking = chiTietDatBanRepository.findByBanMaBanAndHoaDonTrangThai(maBan, false)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn/đặt bàn đang hoạt động để hủy cho bàn " + ban.getTenBan()));
        HoaDon activeHoaDon = activeBooking.getHoaDon();

        // 3. Xóa tất cả ChiTietHoaDon liên quan đến hóa đơn này
        List<ChiTietHoaDon> itemsToDelete = chiTietHoaDonRepository.findByHoaDonMaHoaDon(activeHoaDon.getMaHoaDon());
        if (!itemsToDelete.isEmpty()) {
            chiTietHoaDonRepository.deleteAll(itemsToDelete);
            System.out.println("Đã xóa " + itemsToDelete.size() + " chi tiết hóa đơn.");
        }

        // 4. Xóa bản ghi ChiTietDatBan
        chiTietDatBanRepository.delete(activeBooking);
        System.out.println("Đã xóa chi tiết đặt bàn.");


        // 5. Xóa Hóa đơn
        hoaDonRepository.delete(activeHoaDon);
        System.out.println("Đã xóa hóa đơn.");

        // 6. Cập nhật trạng thái bàn thành "Trống"
        ban.setTinhTrang("Trống");
        banRepository.save(ban);
        System.out.println("Đã cập nhật trạng thái bàn thành Trống.");

        System.out.println("Đã hủy thành công bàn " + ban.getTenBan());
    }


    /**
     * PHƯƠNG THỨC MỚI: Xử lý tách bàn
     */
    @Transactional
    public void splitTable(String sourceTableId, String destinationTableId, List<SplitTableRequestDTO.SplitItemDTO> itemsToMove) {
        // 1. Validation
        if (itemsToMove == null || itemsToMove.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn món cần tách.");
        }
        Ban sourceTable = banRepository.findById(sourceTableId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn nguồn: " + sourceTableId));
        Ban destinationTable = banRepository.findById(destinationTableId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn đích: " + destinationTableId));
        if ("Trống".equalsIgnoreCase(sourceTable.getTinhTrang())) {
            throw new IllegalArgumentException("Không thể tách từ bàn trống.");
        }
        if (!"Trống".equalsIgnoreCase(destinationTable.getTinhTrang())) {
            throw new IllegalArgumentException("Bàn đích phải là bàn trống.");
        }

        // 2. Lấy hóa đơn hoạt động của bàn nguồn
        ChiTietDatBan sourceBooking = chiTietDatBanRepository.findByBanMaBanAndHoaDonTrangThai(sourceTableId, false)
                .orElseThrow(() -> new RuntimeException("Bàn nguồn " + sourceTable.getTenBan() + " không có hóa đơn/đặt bàn hoạt động."));
        HoaDon sourceHoaDon = sourceBooking.getHoaDon();
        NhanVien responsibleNhanVien = sourceBooking.getNhanVien(); // Nhân viên phụ trách

        // 3. Tạo Hóa đơn MỚI cho bàn đích
        HoaDon destinationHoaDon = new HoaDon();
        destinationHoaDon.setNgayGioTao(LocalDateTime.now());
        destinationHoaDon.setTrangThai(false); // Chưa thanh toán
        destinationHoaDon.setTongTien(BigDecimal.ZERO); // Bắt đầu từ 0
        destinationHoaDon = hoaDonRepository.save(destinationHoaDon);

        // 4. Tạo ChiTietDatBan MỚI cho bàn đích
        ChiTietDatBanId destBookingId = new ChiTietDatBanId();
        destBookingId.setMaBan(destinationTableId);
        destBookingId.setMaNhanVien(responsibleNhanVien.getMaNhanVien());
        destBookingId.setMaHoaDon(destinationHoaDon.getMaHoaDon());

        ChiTietDatBan destBooking = new ChiTietDatBan();
        destBooking.setId(destBookingId);
        destBooking.setBan(destinationTable);
        destBooking.setNhanVien(responsibleNhanVien);
        destBooking.setHoaDon(destinationHoaDon);
        destBooking.setTenKhachHang("Tách từ " + sourceTable.getTenBan()); // Tên khách
        destBooking.setNgayGioDat(LocalDateTime.now());
        chiTietDatBanRepository.save(destBooking);


        // 5. Xử lý chuyển món
        BigDecimal sourceBillTotal = sourceHoaDon.getTongTien();
        BigDecimal destBillTotal = BigDecimal.ZERO;
        List<ChiTietHoaDon> sourceItems = chiTietHoaDonRepository.findByHoaDonMaHoaDon(sourceHoaDon.getMaHoaDon());
        List<ChiTietHoaDon> itemsToDeleteFromSource = new ArrayList<>(); // Lưu món cần xóa khỏi nguồn
        List<ChiTietHoaDon> itemsToSaveForDest = new ArrayList<>(); // Lưu món mới cho đích

        for (SplitTableRequestDTO.SplitItemDTO itemToMove : itemsToMove) {
            if (itemToMove.getSoLuong() <= 0) continue; // Bỏ qua nếu số lượng không hợp lệ

            // Tìm món tương ứng trong hóa đơn nguồn
            ChiTietHoaDon sourceDetail = sourceItems.stream()
                    .filter(detail -> detail.getId().getMaThucDon().equals(itemToMove.getMaThucDon()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Món ăn " + itemToMove.getMaThucDon() + " không có trong bàn nguồn."));

            int quantityToMove = itemToMove.getSoLuong();
            int currentSourceQuantity = sourceDetail.getSoLuong();

            if (quantityToMove > currentSourceQuantity) {
                throw new IllegalArgumentException("Số lượng tách (" + quantityToMove + ") của món '" + sourceDetail.getThucDon().getTenMon() + "' lớn hơn số lượng hiện có (" + currentSourceQuantity + ").");
            }

            BigDecimal price = sourceDetail.getGiaTaiThoiDiemBan();
            BigDecimal amountToMove = price.multiply(BigDecimal.valueOf(quantityToMove));

            // Cập nhật hóa đơn đích
            destBillTotal = destBillTotal.add(amountToMove);
            ChiTietHoaDon destDetail = new ChiTietHoaDon();
            ChiTietHoaDonId destDetailId = new ChiTietHoaDonId();
            destDetailId.setMaHoaDon(destinationHoaDon.getMaHoaDon());
            destDetailId.setMaThucDon(sourceDetail.getId().getMaThucDon());
            destDetail.setId(destDetailId);
            destDetail.setHoaDon(destinationHoaDon);
            destDetail.setThucDon(sourceDetail.getThucDon());
            destDetail.setSoLuong(quantityToMove);
            destDetail.setGiaTaiThoiDiemBan(price);
            destDetail.setThanhTien(amountToMove);
            itemsToSaveForDest.add(destDetail);

            // Cập nhật hóa đơn nguồn
            sourceBillTotal = sourceBillTotal.subtract(amountToMove);
            sourceDetail.setSoLuong(currentSourceQuantity - quantityToMove);
            sourceDetail.setThanhTien(sourceDetail.getThanhTien().subtract(amountToMove));

            // Nếu số lượng nguồn về 0, đánh dấu để xóa
            if (sourceDetail.getSoLuong() == 0) {
                itemsToDeleteFromSource.add(sourceDetail);
                // Loại bỏ khỏi list sourceItems để không save lại
                // Cần dùng Iterator để tránh ConcurrentModificationException
                // Hoặc đơn giản là không saveAll mà chỉ save các item còn lại
            }
        } // Kết thúc vòng lặp món cần tách

        // 6. Lưu các thay đổi
        chiTietHoaDonRepository.saveAll(itemsToSaveForDest); // Lưu món mới cho bàn đích
        chiTietHoaDonRepository.deleteAll(itemsToDeleteFromSource); // Xóa món số lượng 0 khỏi bàn nguồn
        // Lưu lại các món còn lại của bàn nguồn (nếu không dùng saveAll cho sourceItems)
        List<ChiTietHoaDon> remainingSourceItems = sourceItems.stream()
                .filter(item -> item.getSoLuong() > 0)
                .collect(Collectors.toList());
        chiTietHoaDonRepository.saveAll(remainingSourceItems);


        sourceHoaDon.setTongTien(sourceBillTotal);
        destinationHoaDon.setTongTien(destBillTotal);
        hoaDonRepository.save(sourceHoaDon);
        hoaDonRepository.save(destinationHoaDon);

        // 7. Cập nhật trạng thái bàn đích
        destinationTable.setTinhTrang("Có khách");
        banRepository.save(destinationTable);

        // 8. Kiểm tra bàn nguồn còn món không để quyết định trạng thái
        if (remainingSourceItems.isEmpty()) {
            sourceTable.setTinhTrang("Trống");
            // Nếu không còn món, có thể xóa luôn ChiTietDatBan của bàn nguồn? (Tùy logic)
            // chiTietDatBanRepository.delete(sourceBooking);
            banRepository.save(sourceTable);
        } // Nếu còn món thì bàn nguồn vẫn là "Có khách"

        System.out.println("Đã tách thành công từ bàn " + sourceTable.getTenBan() + " sang bàn " + destinationTable.getTenBan());
    }

    @Transactional
    public void mergeTables(List<String> sourceTableIds, String destinationTableId) {
        // 1. Validation cơ bản
        if (sourceTableIds == null || sourceTableIds.isEmpty()) {
            throw new IllegalArgumentException("Cần chọn ít nhất 1 bàn nguồn để gộp.");
        }
        if (destinationTableId == null) {
            throw new IllegalArgumentException("Chưa chọn bàn đích.");
        }
        if (sourceTableIds.size() == 1 && sourceTableIds.get(0).equals(destinationTableId)) {
            throw new IllegalArgumentException("Không thể gộp một bàn vào chính nó.");
        }


        // 2. Lấy thông tin bàn đích
        Ban destinationTable = banRepository.findById(destinationTableId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn đích: " + destinationTableId));

        HoaDon destinationHoaDon;
        ChiTietDatBan destBooking = null; // Sẽ cần nếu đích không trống
        NhanVien firstSourceNhanVien = null; // Cần để tạo booking mới nếu đích trống
        Map<String, ChiTietHoaDon> destinationItemsMap = new HashMap<>(); // Lưu món của bàn đích
        boolean isDestinationOriginallyEmpty = "Trống".equalsIgnoreCase(destinationTable.getTinhTrang());

        // 3. Xử lý dựa trên trạng thái bàn đích
        if (isDestinationOriginallyEmpty) {
            // --- TRƯỜNG HỢP 1: BÀN ĐÍCH TRỐNG ---
            System.out.println("Gộp vào bàn trống: " + destinationTable.getTenBan());
            // Tạo Hóa đơn MỚI cho bàn đích
            destinationHoaDon = new HoaDon();
            destinationHoaDon.setNgayGioTao(LocalDateTime.now());
            destinationHoaDon.setTrangThai(false);
            destinationHoaDon.setTongTien(BigDecimal.ZERO);
            destinationHoaDon = hoaDonRepository.save(destinationHoaDon); // Lưu để lấy ID
        } else {
            // --- TRƯỜNG HỢP 2: BÀN ĐÍCH ĐÃ CÓ KHÁCH/ĐẶT TRƯỚC ---
            System.out.println("Gộp vào bàn có khách/đặt trước: " + destinationTable.getTenBan());
            // Tìm Hóa đơn hiện có của bàn đích
            destBooking = chiTietDatBanRepository.findByBanMaBanAndHoaDonTrangThai(destinationTableId, false)
                    .orElseThrow(() -> new RuntimeException("Bàn đích ["+ destinationTable.getTenBan() +"] không có hóa đơn/đặt bàn hoạt động."));
            destinationHoaDon = destBooking.getHoaDon();
            // Lấy danh sách món hiện có của bàn đích
            destinationItemsMap = chiTietHoaDonRepository.findByHoaDonMaHoaDon(destinationHoaDon.getMaHoaDon())
                    .stream()
                    .collect(Collectors.toMap(cthd -> cthd.getId().getMaThucDon(), cthd -> cthd));
        }

        BigDecimal totalMergedAmount = BigDecimal.ZERO; // Chỉ tính tiền từ các bàn nguồn
        List<HoaDon> sourceHoaDonsToDelete = new ArrayList<>();
        String representativeCustomerName = isDestinationOriginallyEmpty ? "Gộp bàn" : destBooking.getTenKhachHang(); // Tên khách

        // 4. Lặp qua các bàn nguồn để xử lý gộp
        for (String sourceId : sourceTableIds) {
            // Bỏ qua nếu bàn nguồn chính là bàn đích (chỉ xảy ra khi đích không trống)
            if (sourceId.equals(destinationTableId)) {
                continue;
            }

            Ban sourceTable = banRepository.findById(sourceId)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy bàn nguồn: " + sourceId));
            ChiTietDatBan sourceBooking = chiTietDatBanRepository.findByBanMaBanAndHoaDonTrangThai(sourceId, false)
                    .orElseThrow(() -> new RuntimeException("Bàn nguồn " + sourceTable.getTenBan() + " không có hóa đơn/đặt bàn hoạt động."));
            HoaDon sourceHoaDon = sourceBooking.getHoaDon();
            sourceHoaDonsToDelete.add(sourceHoaDon);

            // Lấy nhân viên từ bàn nguồn đầu tiên (chỉ cần nếu đích trống)
            if (isDestinationOriginallyEmpty && firstSourceNhanVien == null) {
                firstSourceNhanVien = sourceBooking.getNhanVien();
                // Lấy tên khách từ bàn nguồn đầu tiên nếu muốn
                // representativeCustomerName = sourceBooking.getTenKhachHang();
            }

            // Lấy chi tiết món ăn của hóa đơn nguồn
            List<ChiTietHoaDon> sourceItems = chiTietHoaDonRepository.findByHoaDonMaHoaDon(sourceHoaDon.getMaHoaDon());

            for (ChiTietHoaDon sourceItem : sourceItems) {
                totalMergedAmount = totalMergedAmount.add(sourceItem.getThanhTien()); // Cộng dồn tiền từ nguồn

                String maThucDon = sourceItem.getId().getMaThucDon();
                // Kiểm tra xem món này đã có ở bàn đích (trong map) chưa
                if (destinationItemsMap.containsKey(maThucDon)) {
                    // Đã có -> Cập nhật số lượng và thành tiền
                    ChiTietHoaDon destItem = destinationItemsMap.get(maThucDon);
                    destItem.setSoLuong(destItem.getSoLuong() + sourceItem.getSoLuong());
                    destItem.setThanhTien(destItem.getThanhTien().add(sourceItem.getThanhTien()));
                } else {
                    // Chưa có -> Tạo ChiTietHoaDon mới cho hóa đơn đích
                    ChiTietHoaDon newItem = new ChiTietHoaDon();
                    ChiTietHoaDonId newItemId = new ChiTietHoaDonId();
                    newItemId.setMaHoaDon(destinationHoaDon.getMaHoaDon()); // ID hóa đơn đích
                    newItemId.setMaThucDon(maThucDon);

                    newItem.setId(newItemId);
                    newItem.setHoaDon(destinationHoaDon); // Liên kết hóa đơn đích
                    newItem.setThucDon(sourceItem.getThucDon());
                    newItem.setSoLuong(sourceItem.getSoLuong());
                    newItem.setGiaTaiThoiDiemBan(sourceItem.getGiaTaiThoiDiemBan());
                    newItem.setThanhTien(sourceItem.getThanhTien());

                    destinationItemsMap.put(maThucDon, newItem); // Thêm vào map để lưu sau
                }
            }

            // 5. Xóa ChiTietDatBan của bàn nguồn
            chiTietDatBanRepository.delete(sourceBooking);

            // 6. Cập nhật trạng thái bàn nguồn thành "Trống"
            sourceTable.setTinhTrang("Trống");
            banRepository.save(sourceTable);
        } // Kết thúc vòng lặp bàn nguồn

        // 7. Nếu bàn đích ban đầu trống, tạo ChiTietDatBan mới cho nó
        if (isDestinationOriginallyEmpty) {
            if (firstSourceNhanVien == null && !sourceTableIds.isEmpty()) {
                // Cố gắng lấy nhân viên từ bàn nguồn cuối cùng (nếu chỉ có 1 bàn nguồn và nó là bàn đích thì sẽ lỗi)
                // Cần đảm bảo logic lấy firstSourceNhanVien chạy đúng
                ChiTietDatBan lastSourceBooking = chiTietDatBanRepository.findByBanMaBanAndHoaDonTrangThai(sourceTableIds.get(sourceTableIds.size()-1), false).orElse(null);
                if(lastSourceBooking!=null) firstSourceNhanVien = lastSourceBooking.getNhanVien();
            }
            if (firstSourceNhanVien == null) {
                throw new RuntimeException("Không thể xác định nhân viên phụ trách.");
            }

            ChiTietDatBanId destBookingId = new ChiTietDatBanId();
            destBookingId.setMaBan(destinationTableId);
            destBookingId.setMaNhanVien(firstSourceNhanVien.getMaNhanVien());
            destBookingId.setMaHoaDon(destinationHoaDon.getMaHoaDon()); // Hóa đơn mới đã lưu

            ChiTietDatBan newDestBooking = new ChiTietDatBan();
            newDestBooking.setId(destBookingId);
            newDestBooking.setBan(destinationTable);
            newDestBooking.setNhanVien(firstSourceNhanVien);
            newDestBooking.setHoaDon(destinationHoaDon);
            newDestBooking.setTenKhachHang(representativeCustomerName);
            newDestBooking.setNgayGioDat(LocalDateTime.now());
            chiTietDatBanRepository.save(newDestBooking);
        }


        // 8. Lưu tất cả các ChiTietHoaDon đã cập nhật/mới tạo của hóa đơn đích
        chiTietHoaDonRepository.saveAll(destinationItemsMap.values());

        // 9. Cập nhật tổng tiền cho hóa đơn đích
        // Cộng thêm tiền từ các bàn nguồn vào tổng tiền hiện có (nếu có)
        destinationHoaDon.setTongTien( (destinationHoaDon.getTongTien() == null ? BigDecimal.ZERO : destinationHoaDon.getTongTien()).add(totalMergedAmount) );
        hoaDonRepository.save(destinationHoaDon);

        // 10. Cập nhật trạng thái bàn đích (nếu ban đầu trống)
        if (isDestinationOriginallyEmpty) {
            destinationTable.setTinhTrang("Có khách"); // Chuyển thành có khách
            banRepository.save(destinationTable);
        }

        // 11. Xóa các hóa đơn nguồn (và chi tiết của chúng)
        chiTietHoaDonRepository.deleteAllByHoaDonIn(sourceHoaDonsToDelete);
        hoaDonRepository.deleteAll(sourceHoaDonsToDelete);

        System.out.println("Đã gộp thành công vào bàn " + destinationTable.getTenBan());
    }

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
                            .map(ct -> new OrderItemDTO(
                                    ct.getId().getMaThucDon(),
                                    ct.getThucDon().getTenMon(),
                                    ct.getSoLuong()))
                            .collect(Collectors.toList())
            );

        } else {
            // Nếu không có hóa đơn đang hoạt động (bàn trống hoặc chỉ đặt trước chưa gọi món)
            details.setOrderedItems(Collections.emptyList());
        }

        return details;
    }
}
