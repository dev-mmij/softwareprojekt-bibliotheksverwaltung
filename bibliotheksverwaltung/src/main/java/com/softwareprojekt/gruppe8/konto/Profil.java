package com.softwareprojekt.gruppe8.konto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "profil")
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profil_id")
    private Long profilId;

    @Lob
    @Column(name = "profilbild")
    private byte[] profilbild;

    @Column(name = "anzeigename")
    private String anzeigename;

    @Column(name = "vorname")
    private String vorname;

    @Column(name = "nachname")
    private String nachname;

    @Column(name = "kurze_bio", length = 1000)
    private String kurzeBio;

    @Column(name = "standort")
    private String standort;

    /**
     * Wird von JPA benötigt.
     */
    protected Profil() {
    }

    public Profil(byte[] profilbild, String anzeigename,
                  String vorname, String nachname, String kurzeBio,
                  String standort) {
        this.profilbild = profilbild;
        this.anzeigename = anzeigename;
        this.vorname = vorname;
        this.nachname = nachname;
        this.kurzeBio = kurzeBio;
        this.standort = standort;
    }

    public Long getProfilId() {
        return profilId;
    }

    public byte[] getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(byte[] profilbild) {
        this.profilbild = profilbild;
    }

    public String getAnzeigename() {
        return anzeigename;
    }

    public void setAnzeigename(String anzeigename) {
        this.anzeigename = anzeigename;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getKurzeBio() {
        return kurzeBio;
    }

    public void setKurzeBio(String kurzeBio) {
        this.kurzeBio = kurzeBio;
    }

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }

    public String getVollstaendigerName() {
        return vorname + " " + nachname;
    }
}