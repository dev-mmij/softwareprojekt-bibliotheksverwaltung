package com.softwareprojekt.gruppe8.marktplatz.entity;

import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity 
@Table(name = "angebot")

public class Angebot {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "angebot_id")
    private Long id;

    public enum zustand{
        WieNeu, SehrGut, Gut, Akzeptabel
    }

    public enum Status{
        Aktiv, Verkauft, Reserviert
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "verkaeufer_id", nullable = false)
    private User verkaeufer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "buch_id", nullable = false)
    private Buch buch;
    
    @Column (name = "preis", nullable = false)
    private BigDecimal preis;

    @Column (name = "beschreibung")
    private String beschreibung;

    @Enumerated(EnumType.STRING)
    @Column(name = "zustand", nullable = false)
    private zustand zustand;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.Aktiv;

    public Angebot() {}
    public Angebot(User verkaeufer, Buch buch, BigDecimal preis, String beschreibung, zustand zustand) {
        this.verkaeufer = verkaeufer;
        this.buch = buch;
        this.preis = preis;
        this.beschreibung = beschreibung;
        this.zustand = zustand;
    }

    public Long getId() {
        return id;
    }

    public User getVerkaeufer() {
        return verkaeufer;
    }

    public Buch getBuch() {
        return buch;
    }

    public void setBuch(Buch buch) {
        this.buch = buch;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public zustand getZustand() {
        return zustand;
    }

    public void setZustand(zustand zustand) {
        this.zustand = zustand;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}


    