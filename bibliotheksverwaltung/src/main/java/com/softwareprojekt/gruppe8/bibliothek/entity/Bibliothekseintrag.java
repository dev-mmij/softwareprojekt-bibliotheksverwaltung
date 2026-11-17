package com.softwareprojekt.gruppe8.bibliothek.entity;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bibliothekseintrag")
// TODO: sobald benutzer da ist, Unique-Constraint ergaenzen:
// @Table(name = "bibliothekseintrag",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"benutzer_id", "buch_id"}))
public class Bibliothekseintrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eintrag_id")
    private Long id;

    // TODO: benutzer (User), sobald User in konto liegt

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