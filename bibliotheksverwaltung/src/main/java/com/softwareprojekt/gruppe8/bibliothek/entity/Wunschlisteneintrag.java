package com.softwareprojekt.gruppe8.bibliothek.entity;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

// Beziehung "wuenscht sich" (User m:n Buch) mit Attribut hinzugefuegt_am
@Entity
@Table(name = "wunschliste",
        uniqueConstraints = @UniqueConstraint(columnNames = {"benutzer_id", "buch_id"}))
public class Wunschlisteneintrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wunsch_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private User benutzer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buch_id", nullable = false)
    private Buch buch;

    @Column(name = "hinzugefuegt_am", nullable = false)
    private LocalDate hinzugefuegtAm;

    public Wunschlisteneintrag() {}

    // Getter & Setter
    public Long getId() { return id; }

    public User getBenutzer() { return benutzer; }

    public void setBenutzer(User benutzer) { this.benutzer = benutzer; }

    public Buch getBuch() { return buch; }

    public void setBuch(Buch buch) { this.buch = buch; }

    public LocalDate getHinzugefuegtAm() { return hinzugefuegtAm; }

    public void setHinzugefuegtAm(LocalDate hinzugefuegtAm) { this.hinzugefuegtAm = hinzugefuegtAm; }
}