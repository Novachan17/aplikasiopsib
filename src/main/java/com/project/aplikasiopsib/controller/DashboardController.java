package com.project.aplikasiopsib.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ⬅️ INI YANG WAJIB
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        model.addAttribute("title", "Dashboard");
        model.addAttribute("content", "dashboard");
        model.addAttribute("activeMenu", "dashboard");

        return "layout/main";
    }

    @GetMapping("/guest/dashboard")
    public String guestDashboard(HttpSession session, Model model) {

        if (!"guest".equals(session.getAttribute("role"))) {
            return "redirect:/";
        }

        model.addAttribute("title", "Dashboard Pengunjung");
        model.addAttribute("content", "dashboard-guest");
        model.addAttribute("activeMenu", "konsultasi");

        return "layout/main";
    }

}
