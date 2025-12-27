package com.project.aplikasiopsib.repository;

import com.project.aplikasiopsib.model.Gejala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GejalaRepository extends JpaRepository<Gejala, String> {

    // =====================================================
    // 🔍 SEARCH + URUT BERDASARKAN ID NUMERIK (G1, G2, G10)
    // =====================================================
    @Query("""
                SELECT g FROM Gejala g
                WHERE g.id_gejala LIKE %:keyword%
                   OR LOWER(g.nama_gejala) LIKE LOWER(CONCAT('%', :keyword, '%'))
                ORDER BY CAST(SUBSTRING(g.id_gejala, 2) AS integer)
            """)
    List<Gejala> search(@Param("keyword") String keyword);

    // =====================================================
    // 📋 LIST SEMUA DATA (URUT NORMAL, TIDAK LONCAT)
    // =====================================================
    @Query("""
                SELECT g FROM Gejala g
                ORDER BY CAST(SUBSTRING(g.id_gejala, 2) AS integer)
            """)
    List<Gejala> findAllOrdered();

    // =====================================================
    // 🔢 AMBIL ID TERAKHIR (UNTUK AUTO GENERATE G1, G2, ...)
    // =====================================================
    @Query("""
                SELECT g.id_gejala FROM Gejala g
                ORDER BY CAST(SUBSTRING(g.id_gejala, 2) AS integer) DESC
                LIMIT 1
            """)
    String findLastId();
}
