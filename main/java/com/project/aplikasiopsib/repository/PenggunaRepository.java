package com.project.aplikasiopsib.repository;

import com.project.aplikasiopsib.model.Pengguna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PenggunaRepository extends JpaRepository<Pengguna, String> {

    // =========================
    // LOGIN
    // =========================
    Pengguna findByUsernameAndPassword(String username, String password);

    // =========================
    // AMBIL ID TERAKHIR (P1, P2, P3)
    // =========================
    @Query(value = "SELECT id_pengguna FROM pengguna ORDER BY CAST(SUBSTRING(id_pengguna, 2) AS INTEGER) DESC LIMIT 1", nativeQuery = true)
    String findLastId();
}
