package com.softwareprojekt.gruppe8.tagebuch.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tagebucheintrag")
public class Tagebucheintrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate datum;

    @Column(length = 2000)
    private String notiz;

    @Column(nullable = false)
    private Integer geleseneSeiten;

    // Leerer Konstruktor für JPA
    protected Tagebucheintrag() {
    }

    // Konstruktor für neue Tagebucheinträge
    public Tagebucheintrag(LocalDate datum,
                           String notiz,
                           Integer geleseneSeiten) {
        this.datum = datum;
        this.notiz = notiz;
        this.geleseneSeiten = geleseneSeiten;
    }

    // Getter
    public Long getId() {
        return id;
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
