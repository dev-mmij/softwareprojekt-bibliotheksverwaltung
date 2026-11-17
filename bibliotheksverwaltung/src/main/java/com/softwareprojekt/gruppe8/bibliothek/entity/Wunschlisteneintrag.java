package com.softwareprojekt.gruppe8.bibliothek.entity;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

// Beziehung "wuenscht sich" (User m:n Buch) mit Attribut hinzugefuegt_am
@Entity
@Table(name = "wunschliste")
@IdClass(WunschlisteneintragId.class)
public class Wunschlisteneintrag {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private User benutzer;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buch_id", nullable = false)
    private Buch buch;

    @Column(name = "hinzugefuegt_am", nullable = false)
    private LocalDate hinzugefuegtAm;

    public Wunschlisteneintrag() {}

    public Wunschlisteneintrag(User benutzer, Buch buch, LocalDate hinzugefuegtAm) {
        this.benutzer = benutzer;
        this.buch = buch;
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    // Getter & Setter
    public User getBenutzer() { return benutzer; }

    public Buch getBuch() { return buch; }

    public LocalDate getHinzugefuegtAm() { return hinzugefuegtAm; }

    public void setHinzugefuegtAm(LocalDate hinzugefuegtAm) { this.hinzugefuegtAm = hinzugefuegtAm; }
}