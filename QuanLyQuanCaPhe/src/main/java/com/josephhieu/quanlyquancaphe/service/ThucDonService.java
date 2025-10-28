package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.ThucDon;
import com.josephhieu.quanlyquancaphe.repository.ThucDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThucDonService {

    @Autowired
    private ThucDonRepository thucDonRepository;

    public List<ThucDon> getAllThucDonSorted() {
        return thucDonRepository.findAllByOrderByLoaiMonAscTenMonAsc();
    }
}
