package com.softwareprojekt.gruppe8.konto;

/**
 * Repräsentiert das Profil eines authenticated Nutzers.
 */

public class Profil {

    private Long profilId;
    private String profilbild;
    private String anzeigename;
    private String vorname;
    private String nachname;
    private String kurzeBio;
    private String standort;

    public Profil() {
    }

    public Profil(Long profilId, String profilbild, String anzeigename,
                  String vorname, String nachname, String kurzeBio,
                  String standort) {
        this.profilId = profilId;
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

    public void setProfilId(Long profilId) {
        this.profilId = profilId;
    }

    public String getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(String profilbild) {
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