package com.project.aplikasiopsib.controller;

import com.project.aplikasiopsib.model.*;
import com.project.aplikasiopsib.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/konsultasi")
public class CetakKonsultasiController {

    @Autowired
    private KonsultasiRepository konsultasiRepository;

    @Autowired
    private DetailKonsultasiRepository detailKonsultasiRepository;

    @Autowired
    private KerusakanRepository kerusakanRepository;

    @GetMapping("/cetak/{id}")
    public String cetak(@PathVariable String id, Model model) {

        Konsultasi konsultasi = konsultasiRepository.findById(id).orElse(null);
        if (konsultasi == null) {
            return "redirect:/riwayat-konsultasi";
        }

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
        // KIRIM KE VIEW
        // =====================
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

        return "cetak-konsultasi";
    }
}
