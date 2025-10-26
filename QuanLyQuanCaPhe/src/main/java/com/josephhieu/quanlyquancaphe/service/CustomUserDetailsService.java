package com.josephhieu.quanlyquancaphe.service;

import com.josephhieu.quanlyquancaphe.entity.TaiKhoan;
import com.josephhieu.quanlyquancaphe.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetails loadUserByUsername(String tenDangNhap) throws UsernameNotFoundException {

        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new UsernameNotFoundException("Tên đăng nhập không tồn tại: " + tenDangNhap));

        Set<GrantedAuthority> authorities = new HashSet<>();

        String role = "ROLE_" + taiKhoan.getQuyenHan().toUpperCase();

        authorities.add(new SimpleGrantedAuthority(role));

        return User.builder()
                .username(taiKhoan.getTenDangNhap())
                .password(taiKhoan.getMatKhau())
                .authorities(authorities)

                .build();
    }
}
