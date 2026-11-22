package com.softwareprojekt.gruppe8.tagebuch.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lesechallenge")
public class Lesechallenge {

    // Eindeutige ID der Challenge
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Titel der Challenge
    @Column(nullable = false)
    private String titel;

    // Beschreibung der Challenge
    @Column(length = 2000)
    private String beschreibung;

    // Art der Challenge: Jahr, Monat oder täglich
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ChallengeTyp typ;

    // Beginn der Challenge
    @Column(nullable = false)
    private LocalDate startdatum;

    // Ende der Challenge
    @Column(nullable = false)
    private LocalDate enddatum;

    // Zielwert: Anzahl Bücher oder Seiten pro Tag
    @Column(nullable = false)
    private Integer zielWert;

    // Punkte als Belohnung
    @Column(nullable = false)
    private Integer belohnungPunkte;

    // Name des Abzeichens
    @Column(length = 100)
    private String abzeichenName;

    // Zeigt, ob die Challenge verfügbar ist
    @Column(nullable = false)
    private boolean aktiv;

    // Leerer Konstruktor für JPA
    protected Lesechallenge() {
    }

    // Konstruktor für neue Challenges
    public Lesechallenge(
            String titel,
            String beschreibung,
            ChallengeTyp typ,
            LocalDate startdatum,
            LocalDate enddatum,
            Integer zielWert,
            Integer belohnungPunkte,
            String abzeichenName) {

        this.titel = titel;
        this.beschreibung = beschreibung;
        this.typ = typ;
        this.startdatum = startdatum;
        this.enddatum = enddatum;
        this.zielWert = zielWert;
        this.belohnungPunkte = belohnungPunkte;
        this.abzeichenName = abzeichenName;
        this.aktiv = true;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public ChallengeTyp getTyp() {
        return typ;
    }

    public LocalDate getStartdatum() {
        return startdatum;
    }

    public LocalDate getEnddatum() {
        return enddatum;
    }

    public Integer getZielWert() {
        return zielWert;
    }

    public Integer getBelohnungPunkte() {
        return belohnungPunkte;
    }

    public String getAbzeichenName() {
        return abzeichenName;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    // Setter
    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setTyp(ChallengeTyp typ) {
        this.typ = typ;
    }

    public void setStartdatum(LocalDate startdatum) {
        this.startdatum = startdatum;
    }

    public void setEnddatum(LocalDate enddatum) {
        this.enddatum = enddatum;
    }

    public void setZielWert(Integer zielWert) {
        this.zielWert = zielWert;
    }

    public void setBelohnungPunkte(Integer belohnungPunkte) {
        this.belohnungPunkte = belohnungPunkte;
    }

    public void setAbzeichenName(String abzeichenName) {
        this.abzeichenName = abzeichenName;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }
}