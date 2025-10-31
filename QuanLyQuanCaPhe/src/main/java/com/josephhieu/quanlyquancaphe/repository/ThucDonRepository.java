package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.ThucDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThucDonRepository extends JpaRepository<ThucDon, String> {

    List<ThucDon> findAllByOrderByLoaiMonAscTenMonAsc();

    List<ThucDon> findByTenMonContainingIgnoreCase(String keyword);
}
