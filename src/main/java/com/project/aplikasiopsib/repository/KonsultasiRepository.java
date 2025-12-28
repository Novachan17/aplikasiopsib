package com.project.aplikasiopsib.repository;

import com.project.aplikasiopsib.model.Konsultasi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KonsultasiRepository
        extends JpaRepository<Konsultasi, String> {

    // 🔍 SEARCH BY ID KONSULTASI
    List<Konsultasi> findByIdKonsultasiContainingIgnoreCase(String keyword);

    // (sudah kamu pakai sebelumnya)
    Konsultasi findTopByOrderByIdKonsultasiDesc();
}
