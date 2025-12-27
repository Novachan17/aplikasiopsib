package com.project.aplikasiopsib.repository;

import com.project.aplikasiopsib.model.Konsultasi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KonsultasiRepository
        extends JpaRepository<Konsultasi, String> {

    Konsultasi findTopByOrderByIdKonsultasiDesc();
}
