package com.project.aplikasiopsib.controller;

import com.project.aplikasiopsib.repository.KonsultasiRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RiwayatKonsultasiController {

    @Autowired
    private KonsultasiRepository konsultasiRepository;

    // 🔐 cek login (Admin / Mekanik / Asisten)
    private boolean isLogin(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping("/riwayat-konsultasi")
    public String riwayat(
            @RequestParam(required = false) String keyword,
            Model model,
            HttpSession session) {

        if (!isLogin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("title", "Riwayat Konsultasi");
        model.addAttribute("content", "riwayat-konsultasi");
        model.addAttribute("activeMenu", "riwayat");

        // 🔍 LOGIKA SEARCH
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute(
                    "list",
                    konsultasiRepository
                            .findByIdKonsultasiContainingIgnoreCase(keyword));
        } else {
            model.addAttribute("list", konsultasiRepository.findAll());
        }

        // supaya value input tidak hilang
        model.addAttribute("keyword", keyword);

        return "layout/main";
    }
}
