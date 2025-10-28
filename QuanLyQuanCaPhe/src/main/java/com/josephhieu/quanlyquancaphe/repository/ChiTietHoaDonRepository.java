package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ChiTietHoaDon;
import com.josephhieu.quanlyquancaphe.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon,String> {

    List<ChiTietHoaDon> findByHoaDonMaHoaDon(String maHoaDon);

    void deleteAllByHoaDonIn(List<HoaDon> hoaDons);
}
