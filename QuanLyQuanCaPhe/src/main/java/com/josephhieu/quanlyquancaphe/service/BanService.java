package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.Ban;
import com.josephhieu.quanlyquancaphe.repository.BanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BanService {

    @Autowired
    private BanRepository banRepository;

    public List<Ban> getAllBan() {

        return banRepository.findAll();
    }
}
