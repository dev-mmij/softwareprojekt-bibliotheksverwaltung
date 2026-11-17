package com.softwareprojekt.gruppe8.bibliothek.entity;

import com.softwareprojekt.gruppe8.konto.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "regal",
        uniqueConstraints = @UniqueConstraint(columnNames = {"benutzer_id", "name"}))
public class Regal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regal_id")
    private Long id;

    // Beziehung "legt an" (User 1:n Regal)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private User benutzer;

    @Column(nullable = false)
    private String name;

    private String beschreibung;

    @Column(name = "erstellt_am", nullable = false)
    private LocalDate erstelltAm;

    // Beziehung "enthaelt" (Regal m:n Bibliothekseintrag)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "regal_eintrag",
            joinColumns = @JoinColumn(name = "regal_id"),
            inverseJoinColumns = @JoinColumn(name = "eintrag_id")
    )
    private Set<Bibliothekseintrag> eintraege = new HashSet<>();

    public Regal() {}

    // Getter & Setter
    public Long getId() { return id; }

    public User getBenutzer() { return benutzer; }

    public void setBenutzer(User benutzer) { this.benutzer = benutzer; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getBeschreibung() { return beschreibung; }

    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }

    public LocalDate getErstelltAm() { return erstelltAm; }

    public void setErstelltAm(LocalDate erstelltAm) { this.erstelltAm = erstelltAm; }

    public Set<Bibliothekseintrag> getEintraege() { return eintraege; }

    public void setEintraege(Set<Bibliothekseintrag> eintraege) { this.eintraege = eintraege; }
}