package com.project.aplikasiopsib.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "konsultasi")
public class Konsultasi {

    @Id
    @Column(name = "id_konsultasi")
    private String idKonsultasi;

    private LocalDateTime tanggal;

    @ManyToOne
    @JoinColumn(name = "id_pengguna")
    private Pengguna pengguna;

    @ManyToOne
    @JoinColumn(name = "id_kerusakan_hasil")
    private Kerusakan kerusakanHasil;

    // ===== GETTER SETTER =====
    public String getIdKonsultasi() {
        return idKonsultasi;
    }

    public void setIdKonsultasi(String idKonsultasi) {
        this.idKonsultasi = idKonsultasi;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public Kerusakan getKerusakanHasil() {
        return kerusakanHasil;
    }

    public void setKerusakanHasil(Kerusakan kerusakanHasil) {
        this.kerusakanHasil = kerusakanHasil;
    }
}
