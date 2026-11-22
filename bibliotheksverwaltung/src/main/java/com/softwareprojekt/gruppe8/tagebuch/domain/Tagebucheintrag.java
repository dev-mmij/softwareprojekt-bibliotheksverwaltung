package com.softwareprojekt.gruppe8.tagebuch.domain;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tagebucheintrag")
public class Tagebucheintrag {

    // Eindeutige ID des Tagebucheintrags
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID des Benutzers, dem der Eintrag gehört
    @Column(name = "benutzer_id", nullable = false)
    private Long benutzerId;

    // Buch, zu dem der Tagebucheintrag gehört
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buch_id", nullable = false)
    private Buch buch;

    // Datum des Leseeintrags
    @Column(nullable = false)
    private LocalDate datum;

    // Persönliche Notiz zum Buch
    @Column(length = 2000)
    private String notiz;

    // Anzahl der gelesenen Seiten
    @Column(nullable = false)
    private Integer geleseneSeiten;

    // Leerer Konstruktor für JPA
    protected Tagebucheintrag() {
    }

    // Konstruktor für neue Tagebucheinträge
    public Tagebucheintrag(Long benutzerId,
                           Buch buch,
                           LocalDate datum,
                           String notiz,
                           Integer geleseneSeiten) {
        this.benutzerId = benutzerId;
        this.buch = buch;
        this.datum = datum;
        this.notiz = notiz;
        this.geleseneSeiten = geleseneSeiten;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public Long getBenutzerId() {
        return benutzerId;
    }

    public Buch getBuch() {
        return buch;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public String getNotiz() {
        return notiz;
    }

    public Integer getGeleseneSeiten() {
        return geleseneSeiten;
    }

    // Setter
    public void setBenutzerId(Long benutzerId) {
        this.benutzerId = benutzerId;
    }

    public void setBuch(Buch buch) {
        this.buch = buch;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }

    public void setGeleseneSeiten(Integer geleseneSeiten) {
        this.geleseneSeiten = geleseneSeiten;
    }
}
