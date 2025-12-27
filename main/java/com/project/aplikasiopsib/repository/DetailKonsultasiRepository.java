package com.project.aplikasiopsib.repository;

import com.project.aplikasiopsib.model.DetailKonsultasi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailKonsultasiRepository
        extends JpaRepository<DetailKonsultasi, Integer> {

    // ✅ SESUAI FIELD ENTITY
    List<DetailKonsultasi> findByKonsultasi_IdKonsultasi(String idKonsultasi);
}
