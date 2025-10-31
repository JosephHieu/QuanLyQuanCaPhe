package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ChiTietThucDon;
import com.josephhieu.quanlyquancaphe.entity.ThucDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietThucDonRepository extends JpaRepository<ChiTietThucDon,String> {

    void deleteAllByThucDonMaThucDon(String maThucDon);

    List<ChiTietThucDon> findAllByThucDonMaThucDon(String maThucDon);
}
