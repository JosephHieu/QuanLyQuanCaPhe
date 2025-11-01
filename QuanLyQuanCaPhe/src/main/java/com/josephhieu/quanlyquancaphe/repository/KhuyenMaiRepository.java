package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.KhuyenMai;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai,String> {

    List<KhuyenMai> findByTenKhuyenMaiContainingIgnoreCase(String keyword);

    List<KhuyenMai> findAll(Sort sort);
}
