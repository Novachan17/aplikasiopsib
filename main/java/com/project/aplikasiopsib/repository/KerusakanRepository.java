package com.project.aplikasiopsib.repository;

import com.project.aplikasiopsib.model.Kerusakan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KerusakanRepository
        extends JpaRepository<Kerusakan, String> {

    @Query("""
                SELECT k.id_kerusakan
                FROM Kerusakan k
                ORDER BY CAST(SUBSTRING(k.id_kerusakan,2) AS integer) DESC
                LIMIT 1
            """)
    String findLastId();

    // 🔴 INI SATU-SATUNYA TAMBAHAN WAJIB
    @Query("""
                SELECT k FROM Kerusakan k
                WHERE LOWER(k.id_kerusakan) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(k.nama_kerusakan) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Kerusakan> search(@Param("keyword") String keyword);
}
