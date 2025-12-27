package com.project.aplikasiopsib.controller;

import com.project.aplikasiopsib.model.Pengguna;
import com.project.aplikasiopsib.repository.PenggunaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private PenggunaRepository penggunaRepository;

    // =============================
    // CEK ADMIN
    // =============================
    private boolean isAdmin(HttpSession session) {
        Pengguna user = (Pengguna) session.getAttribute("user");
        return user != null && "admin".equalsIgnoreCase(user.getRole());
    }

    // =============================
    // LIST USER
    // =============================

    @GetMapping
    public String list(Model model, HttpSession session) {
        if (!isAdmin(session))
            return "redirect:/dashboard";

        model.addAttribute("title", "Kelola User");
        model.addAttribute("content", "admin/users");
        model.addAttribute("users", penggunaRepository.findAll());
        model.addAttribute("activeMenu", "admin-users");

        return "layout/main";
    }

    // =============================
    // FORM TAMBAH
    // =============================
    @GetMapping("/tambah")
    public String tambah(Model model, HttpSession session) {
        if (!isAdmin(session))
            return "redirect:/dashboard";

        model.addAttribute("user", new Pengguna());
        model.addAttribute("title", "Tambah User");
        model.addAttribute("content", "admin/user-form");

        return "layout/main";
    }

    // =============================
    // SIMPAN USER
    // =============================
    @PostMapping("/simpan")
    public String simpan(@ModelAttribute Pengguna user, HttpSession session) {
        if (!isAdmin(session))
            return "redirect:/dashboard";

        if (!"admin".equalsIgnoreCase(user.getRole())
                && !"mekanik".equalsIgnoreCase(user.getRole())
                && !"asisten".equalsIgnoreCase(user.getRole())) {
            user.setRole("asisten"); // default aman
        }

        // AUTO ID P1, P2, P3
        if (user.getId_pengguna() == null || user.getId_pengguna().isEmpty()) {
            String lastId = penggunaRepository.findLastId(); // contoh: P3
            int next = 1;

            if (lastId != null && lastId.startsWith("P")) {
                next = Integer.parseInt(lastId.substring(1)) + 1;
            }

            user.setId_pengguna("P" + next);
        }

        penggunaRepository.save(user);
        return "redirect:/admin/users";
    }

    // =============================
    // EDIT USER
    // =============================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model, HttpSession session) {
        if (!isAdmin(session))
            return "redirect:/dashboard";

        model.addAttribute("user", penggunaRepository.findById(id).orElse(null));
        model.addAttribute("title", "Edit User");
        model.addAttribute("content", "admin/user-form");

        return "layout/main";
    }

    // =============================
    // HAPUS USER
    // =============================
    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable String id, HttpSession session) {
        if (!isAdmin(session))
            return "redirect:/dashboard";

        penggunaRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
