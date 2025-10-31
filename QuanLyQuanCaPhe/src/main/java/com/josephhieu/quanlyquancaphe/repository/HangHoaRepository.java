package com.josephhieu.quanlyquancaphe.repository;

import com.josephhieu.quanlyquancaphe.entity.HangHoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HangHoaRepository extends JpaRepository<HangHoa, String> {

    Optional<HangHoa> findByTenHangHoaIgnoreCase(String tenHangHoa);

    List<HangHoa> findByTenHangHoaContainingIgnoreCase(String keyword);

    List<HangHoa> findAllByOrderByTenHangHoaAsc();
}
