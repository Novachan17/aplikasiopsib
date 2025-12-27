package com.project.aplikasiopsib.controller;

import com.project.aplikasiopsib.model.*;
import com.project.aplikasiopsib.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/konsultasi")
public class KonsultasiController {

    @Autowired
    private GejalaRepository gejalaRepository;

    @Autowired
    private KerusakanRepository kerusakanRepository;

    @Autowired
    private KonsultasiRepository konsultasiRepository;

    @Autowired
    private DetailKonsultasiRepository detailKonsultasiRepository;

    // =====================
    // FORM KONSULTASI
    // =====================
    @GetMapping
    public String form(Model model) {

        model.addAttribute("title", "Konsultasi");
        model.addAttribute("content", "konsultasi");
        model.addAttribute("activeMenu", "konsultasi");
        model.addAttribute("listGejala", gejalaRepository.findAllOrdered());

        return "layout/main";
    }

    // =====================
    // PROSES DIAGNOSA
    // =====================
    @PostMapping("/proses")
    public String proses(
            @RequestParam(name = "gejalaIds") List<String> gejalaIds,
            HttpSession session,
            Model model) {

        List<Map<String, Object>> hasil = new ArrayList<>();

        for (Kerusakan kerusakan : kerusakanRepository.findAll()) {

            int total = kerusakan.getGejalaList().size();
            int cocok = 0;

            for (Gejala g : kerusakan.getGejalaList()) {
                if (gejalaIds.contains(g.getId_gejala())) {
                    cocok++;
                }
            }

            if (cocok > 0) {
                double persentase = ((double) cocok / total) * 100;

                Map<String, Object> row = new HashMap<>();
                row.put("kerusakan", kerusakan);
                row.put("cocok", cocok);
                row.put("total", total);
                row.put("persentase", persentase);

                hasil.add(row);
            }
        }

        hasil.sort((a, b) -> Double.compare(
                (double) b.get("persentase"),
                (double) a.get("persentase")));

        // =====================
        // GENERATE ID KONSULTASI
        // =====================
        Konsultasi last = konsultasiRepository.findTopByOrderByIdKonsultasiDesc();
        int next = (last == null)
                ? 1
                : Integer.parseInt(last.getIdKonsultasi().substring(2)) + 1;

        String idKonsultasi = "KS" + String.format("%04d", next);

        Kerusakan kerusakanHasil = (Kerusakan) hasil.get(0).get("kerusakan");

        Konsultasi konsultasi = new Konsultasi();
        konsultasi.setIdKonsultasi(idKonsultasi);
        konsultasi.setTanggal(LocalDateTime.now());
        konsultasi.setKerusakanHasil(kerusakanHasil);

        if (session.getAttribute("user") != null) {
            konsultasi.setPengguna((Pengguna) session.getAttribute("user"));
        } else {
            konsultasi.setPengguna(null); // guest
        }

        konsultasiRepository.save(konsultasi);

        String namaPelaku;

        if (session.getAttribute("user") != null) {
            namaPelaku = ((Pengguna) session.getAttribute("user")).getNama();
        } else {
            namaPelaku = (String) session.getAttribute("guestName");
        }

        model.addAttribute("namaPelaku", namaPelaku);

        // =====================
        // SIMPAN DETAIL
        // =====================
        for (String idGejala : gejalaIds) {
            DetailKonsultasi detail = new DetailKonsultasi();
            detail.setKonsultasi(konsultasi);
            detail.setGejala(
                    gejalaRepository.findById(idGejala).orElse(null));
            detailKonsultasiRepository.save(detail);
        }

        model.addAttribute("hasil", hasil);
        model.addAttribute("konsultasi", konsultasi);
        model.addAttribute("content", "hasil-konsultasi");

        return "layout/main";
    }

    // =====================
    // DETAIL KONSULTASI
    // =====================
    @GetMapping("/detail/{id}")
    public String detailKonsultasi(
            @PathVariable String id,
            Model model) {

        Konsultasi konsultasi = konsultasiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Konsultasi tidak ditemukan"));

        // gejala yg dipilih
        List<DetailKonsultasi> detailList = detailKonsultasiRepository.findByKonsultasi_IdKonsultasi(id);

        List<Gejala> gejalaDipilih = detailList.stream().map(DetailKonsultasi::getGejala).toList();

        // =====================
        // HITUNG ULANG TOP KANDIDAT
        // =====================
        List<Map<String, Object>> hasil = new ArrayList<>();

        for (Kerusakan kerusakan : kerusakanRepository.findAll()) {

            int total = kerusakan.getGejalaList().size();
            int cocok = 0;

            for (Gejala g : kerusakan.getGejalaList()) {
                if (gejalaDipilih.contains(g)) {
                    cocok++;
                }
            }

            if (cocok > 0) {
                double persentase = ((double) cocok / total) * 100;

                Map<String, Object> row = new HashMap<>();
                row.put("kerusakan", kerusakan);
                row.put("cocok", cocok);
                row.put("total", total);
                row.put("persentase", persentase);

                hasil.add(row);
            }
        }

        hasil.sort((a, b) -> Double.compare(
                (double) b.get("persentase"),
                (double) a.get("persentase")));

        model.addAttribute("title", "Detail Konsultasi");
        model.addAttribute("content", "detail-konsultasi");
        model.addAttribute("activeMenu", "riwayat");

        model.addAttribute("konsultasi", konsultasi);
        model.addAttribute("gejalaList", gejalaDipilih);
        model.addAttribute("hasil", hasil);

        String namaPelaku;

        if (konsultasi.getPengguna() != null) {
            namaPelaku = konsultasi.getPengguna().getNama();
        } else {
            namaPelaku = "Pengguna Umum";
        }

        model.addAttribute("namaPelaku", namaPelaku);

        return "layout/main";
    }

    @GetMapping("/cetak-hasil/{id}")
    public String cetakHasilKonsultasi(
            @PathVariable String id,
            HttpSession session,
            Model model) {

        Konsultasi konsultasi = konsultasiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Konsultasi tidak ditemukan"));

        // =====================
        // GEJALA DIPILIH
        // =====================
        List<DetailKonsultasi> detailList = detailKonsultasiRepository.findByKonsultasi_IdKonsultasi(id);

        List<Gejala> gejalaDipilih = detailList.stream()
                .map(DetailKonsultasi::getGejala)
                .toList();

        // =====================
        // HITUNG ULANG TOP KANDIDAT
        // =====================
        List<Map<String, Object>> hasil = new ArrayList<>();

        for (Kerusakan kerusakan : kerusakanRepository.findAll()) {

            int total = kerusakan.getGejalaList().size();
            int cocok = 0;

            for (Gejala g : kerusakan.getGejalaList()) {
                if (gejalaDipilih.contains(g)) {
                    cocok++;
                }
            }

            if (cocok > 0) {
                double persentase = ((double) cocok / total) * 100;

                Map<String, Object> row = new HashMap<>();
                row.put("kerusakan", kerusakan);
                row.put("cocok", cocok);
                row.put("total", total);
                row.put("persentase", persentase);

                hasil.add(row);
            }
        }

        hasil.sort((a, b) -> Double.compare(
                (double) b.get("persentase"),
                (double) a.get("persentase")));

        // =====================
        // NAMA PELAKU (SESSION)
        // =====================
        String namaPelaku;

        if (session.getAttribute("user") != null) {
            namaPelaku = ((Pengguna) session.getAttribute("user")).getNama();
        } else {
            namaPelaku = (String) session.getAttribute("guestName");
        }

        // =====================
        // KIRIM KE VIEW
        // =====================
        model.addAttribute("konsultasi", konsultasi);
        model.addAttribute("gejalaList", gejalaDipilih);
        model.addAttribute("hasil", hasil);
        model.addAttribute("namaPelaku", namaPelaku);

        return "cetak-hasil-konsultasi";
    }

}
