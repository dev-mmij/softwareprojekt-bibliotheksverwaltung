package com.softwareprojekt.gruppe8.bibliothek.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "regal")
public class Regal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regal_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String beschreibung;

    @Column(name = "erstellt_am", nullable = false)
    private LocalDate erstelltAm;

    // TODO: benutzer (User), sobald User in konto liegt

    // NEU: Beziehung "enthaelt" (Regal m:n Bibliothekseintrag)
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

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getBeschreibung() { return beschreibung; }

    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }

    public LocalDate getErstelltAm() { return erstelltAm; }

    public void setErstelltAm(LocalDate erstelltAm) { this.erstelltAm = erstelltAm; }

    public Set<Bibliothekseintrag> getEintraege() { return eintraege; }

    public void setEintraege(Set<Bibliothekseintrag> eintraege) { this.eintraege = eintraege; }
}