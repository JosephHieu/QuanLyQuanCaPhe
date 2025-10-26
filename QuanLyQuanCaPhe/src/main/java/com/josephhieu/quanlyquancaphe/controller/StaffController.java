package com.josephhieu.quanlyquancaphe.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaffController {

    @GetMapping("/staff/home")
    public String staffHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("username", userDetails.getUsername());

        model.addAttribute("currentPage", "home");
        // Trả về file staff_home.html
        return "home/staff_home";
    }
}