package com.project.aplikasiopsib.controller;

import com.project.aplikasiopsib.model.Kerusakan;
import com.project.aplikasiopsib.repository.GejalaRepository;
import com.project.aplikasiopsib.repository.KerusakanRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/kerusakan")
public class KerusakanController {

    @Autowired
    private KerusakanRepository kerusakanRepository;

    @Autowired
    private GejalaRepository gejalaRepository;

    private boolean isLogin(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    // =====================
    // LIST
    // =====================
    @GetMapping
    public String index(
            @RequestParam(required = false) String keyword,
            Model model,
            HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        List<Kerusakan> list;

        if (keyword != null && !keyword.trim().isEmpty()) {
            list = kerusakanRepository.search(keyword);
        } else {
            list = kerusakanRepository.findAll();
        }

        model.addAttribute("title", "Data Kerusakan");
        model.addAttribute("content", "kerusakan");
        model.addAttribute("activeMenu", "kerusakan");
        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);

        return "layout/main";
    }

    // =====================
    // FORM TAMBAH
    // =====================
    @GetMapping("/tambah")
    public String tambah(Model model, HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        model.addAttribute("kerusakan", new Kerusakan());
        model.addAttribute("listGejala", gejalaRepository.findAll());
        model.addAttribute("title", "Tambah Kerusakan");
        model.addAttribute("content", "kerusakan-form");
        model.addAttribute("activeMenu", "kerusakan");

        return "layout/main";
    }

    // =====================
    // SIMPAN
    // =====================
    @PostMapping("/simpan")
    public String simpan(
            @ModelAttribute Kerusakan kerusakan,
            @RequestParam(required = false) List<String> gejalaIds,
            HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        // AUTO ID K1, K2, K3
        if (kerusakan.getId_kerusakan() == null || kerusakan.getId_kerusakan().isEmpty()) {
            String lastId = kerusakanRepository.findLastId();
            int next = 1;

            if (lastId != null && lastId.startsWith("K")) {
                next = Integer.parseInt(lastId.substring(1)) + 1;
            }
            kerusakan.setId_kerusakan("K" + next);
        }

        if (gejalaIds != null) {
            kerusakan.setGejalaList(
                    gejalaRepository.findAllById(gejalaIds));
        }

        kerusakanRepository.save(kerusakan);
        return "redirect:/kerusakan";
    }

    // =====================
    // EDIT
    // =====================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model, HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        model.addAttribute("kerusakan",
                kerusakanRepository.findById(id).orElse(null));
        model.addAttribute("listGejala", gejalaRepository.findAll());
        model.addAttribute("title", "Edit Kerusakan");
        model.addAttribute("content", "kerusakan-form");
        model.addAttribute("activeMenu", "kerusakan");

        return "layout/main";
    }

    // =====================
    // CETAK
    // =====================
    @GetMapping("/cetak")
    public String cetak(Model model, HttpSession session) {

        if (!isLogin(session))
            return "redirect:/login";

        model.addAttribute("list", kerusakanRepository.findAll());
        return "kerusakan-cetak";
    }

}
