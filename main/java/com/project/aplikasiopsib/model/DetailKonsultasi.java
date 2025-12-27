package com.project.aplikasiopsib.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detail_konsultasi")
public class DetailKonsultasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_detail;

    @ManyToOne
    @JoinColumn(name = "id_konsultasi")
    private Konsultasi konsultasi;

    @ManyToOne
    @JoinColumn(name = "id_gejala")
    private Gejala gejala;

    // ===== GETTER SETTER =====
    public Integer getId_detail() {
        return id_detail;
    }

    public Konsultasi getKonsultasi() {
        return konsultasi;
    }

    public void setKonsultasi(Konsultasi konsultasi) {
        this.konsultasi = konsultasi;
    }

    public Gejala getGejala() {
        return gejala;
    }

    public void setGejala(Gejala gejala) {
        this.gejala = gejala;
    }
}
