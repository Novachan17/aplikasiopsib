package com.project.aplikasiopsib.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "kerusakan")
public class Kerusakan {

    @Id
    private String id_kerusakan;

    private String nama_kerusakan;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(columnDefinition = "TEXT")
    private String solusi;

    @ManyToMany
    @JoinTable(name = "aturan", joinColumns = @JoinColumn(name = "id_kerusakan"), inverseJoinColumns = @JoinColumn(name = "id_gejala"))
    private List<Gejala> gejalaList;

    // ===== GETTER SETTER =====
    public String getId_kerusakan() {
        return id_kerusakan;
    }

    public void setId_kerusakan(String id_kerusakan) {
        this.id_kerusakan = id_kerusakan;
    }

    public String getNama_kerusakan() {
        return nama_kerusakan;
    }

    public void setNama_kerusakan(String nama_kerusakan) {
        this.nama_kerusakan = nama_kerusakan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getSolusi() {
        return solusi;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    public List<Gejala> getGejalaList() {
        return gejalaList;
    }

    public void setGejalaList(List<Gejala> gejalaList) {
        this.gejalaList = gejalaList;
    }
}
