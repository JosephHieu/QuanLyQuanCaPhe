package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.DonViTinh;
import com.josephhieu.quanlyquancaphe.repository.DonViTinhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonViTinhService {

    @Autowired
    private DonViTinhRepository donViTinhRepository;

    public List<DonViTinh> getAllDonViTinh() {

        return donViTinhRepository.findAll(Sort.by("tenDonVi"));
    }
}
