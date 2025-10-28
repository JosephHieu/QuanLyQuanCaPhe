package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ChiTietDatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChiTietDatBanRepository extends JpaRepository<ChiTietDatBan,String> {

    Optional<ChiTietDatBan> findByBanMaBanAndHoaDonTrangThai(String maBan, boolean trangThaiHoaDon);
}
