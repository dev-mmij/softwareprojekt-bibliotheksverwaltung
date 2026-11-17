package com.softwareprojekt.gruppe8.bibliothek.entity;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

@Entity
@Table(name = "bibliothekseintrag",
        uniqueConstraints = @UniqueConstraint(columnNames = {"benutzer_id", "buch_id"}))
public class Bibliothekseintrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eintrag_id")
    private Long id;

    // Beziehung "fuehrt" (User 1:n Bibliothekseintrag)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private User benutzer;

    // Beziehung "verweist auf" (Bibliothekseintrag n:1 Buch)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buch_id", nullable = false)
    private Buch buch;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Lesestatus lesestatus;

    @Column(name = "readlist_position")
    private Integer readlistPosition;

    @Column(name = "hinzugefuegt_am", nullable = false)
    private LocalDate hinzugefuegtAm;

    @Column(name = "begonnen_am")
    private LocalDate begonnenAm;

    @Column(name = "beendet_am")
    private LocalDate beendetAm;

    public Bibliothekseintrag() {}

    // Getter & Setter
    public Long getId() { return id; }

    public User getBenutzer() { return benutzer; }

    public void setBenutzer(User benutzer) { this.benutzer = benutzer; }

    public Buch getBuch() { return buch; }

    public void setBuch(Buch buch) { this.buch = buch; }

    public Lesestatus getLesestatus() { return lesestatus; }

    public void setLesestatus(Lesestatus lesestatus) { this.lesestatus = lesestatus; }

    public Integer getReadlistPosition() { return readlistPosition; }

    public void setReadlistPosition(Integer readlistPosition) { this.readlistPosition = readlistPosition; }

    public LocalDate getHinzugefuegtAm() { return hinzugefuegtAm; }

    public void setHinzugefuegtAm(LocalDate hinzugefuegtAm) { this.hinzugefuegtAm = hinzugefuegtAm; }

    public LocalDate getBegonnenAm() { return begonnenAm; }

    public void setBegonnenAm(LocalDate begonnenAm) { this.begonnenAm = begonnenAm; }

    public LocalDate getBeendetAm() { return beendetAm; }

    public void setBeendetAm(LocalDate beendetAm) { this.beendetAm = beendetAm; }
}