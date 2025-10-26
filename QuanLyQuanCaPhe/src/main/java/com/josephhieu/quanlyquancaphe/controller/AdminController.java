package com.josephhieu.quanlyquancaphe.controller;

import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/home")
    public String adminHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("username", userDetails.getUsername());

        return "home/admin_home";
    }
}
