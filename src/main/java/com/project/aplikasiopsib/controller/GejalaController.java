package com.project.aplikasiopsib.controller;

import com.project.aplikasiopsib.model.Gejala;
import com.project.aplikasiopsib.repository.GejalaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/gejala")
public class GejalaController {

    @Autowired
    private GejalaRepository gejalaRepository;

    // =====================
    // 🔐 CEK LOGIN
    // =====================
    private boolean isLogin(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    // =====================
    // 📋 LIST + SEARCH (URUT FIX)
    // =====================
    @GetMapping
    public String list(
            @RequestParam(required = false) String keyword,
            Model model,
            HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        List<Gejala> list;

        if (keyword == null || keyword.trim().isEmpty()) {
            // ✅ WAJIB pakai ini (biar tidak loncat)
            list = gejalaRepository.findAllOrdered();
        } else {
            // ✅ SEARCH juga sudah ORDER BY
            list = gejalaRepository.search(keyword);
        }

        model.addAttribute("title", "Data Gejala");
        model.addAttribute("content", "gejala");
        model.addAttribute("activeMenu", "gejala");
        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);

        return "layout/main";
    }

    // =====================
    // ➕ FORM TAMBAH
    // =====================
    @GetMapping("/tambah")
    public String tambah(Model model, HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        model.addAttribute("gejala", new Gejala());
        model.addAttribute("title", "Tambah Gejala");
        model.addAttribute("content", "gejala-form");
        model.addAttribute("activeMenu", "gejala");

        return "layout/main";
    }

    // =====================
    // 💾 SIMPAN (AUTO G1, G2, ...)
    // =====================
    @PostMapping("/simpan")
    public String simpan(
            @ModelAttribute Gejala gejala,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isLogin(session))
            return "redirect:/login";

        // AUTO ID HANYA SAAT TAMBAH
        if (gejala.getId_gejala() == null || gejala.getId_gejala().isEmpty()) {

            String lastId = gejalaRepository.findLastId(); // contoh: G27
            int next = 1;

            if (lastId != null && lastId.startsWith("G")) {
                next = Integer.parseInt(lastId.substring(1)) + 1;
            }

            gejala.setId_gejala("G" + next);
        }

        gejalaRepository.save(gejala);

        redirectAttributes.addFlashAttribute(
                "success",
                "Data gejala berhasil disimpan.");

        return "redirect:/gejala";
    }

    // =====================
    // ✏️ FORM EDIT
    // =====================
    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable String id,
            Model model,
            HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        model.addAttribute("gejala",
                gejalaRepository.findById(id).orElse(null));
        model.addAttribute("title", "Edit Gejala");
        model.addAttribute("content", "gejala-form");
        model.addAttribute("activeMenu", "gejala");

        return "layout/main";
    }

    // =====================
    // 🗑️ HAPUS (AMAN)
    // =====================
    @GetMapping("/hapus/{id}")
    public String hapus(
            @PathVariable String id,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        try {
            gejalaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Data gejala berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Data gejala tidak dapat dihapus karena masih digunakan dalam aturan.");
        }

        return "redirect:/gejala";
    }

    // =====================
    // 🖨️ CETAK (URUT)
    // =====================
    @GetMapping("/cetak")
    public String cetak(Model model, HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        // ✅ CETAK JUGA HARUS TERURUT
        model.addAttribute("list", gejalaRepository.findAllOrdered());
        model.addAttribute("title", "Cetak Data Gejala");

        return "gejala-cetak";
    }
}
