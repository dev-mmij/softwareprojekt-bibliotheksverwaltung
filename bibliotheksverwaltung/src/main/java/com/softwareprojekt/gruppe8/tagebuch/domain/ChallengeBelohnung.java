package com.softwareprojekt.gruppe8.tagebuch.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

// Speichert eine erhaltene Challenge-Belohnung
@Entity
@Table(
        name = "challenge_belohnung",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"benutzer_id", "challenge_id"}
        )
)
public class ChallengeBelohnung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Benutzer, der die Belohnung erhalten hat
    @Column(name = "benutzer_id", nullable = false)
    private Long benutzerId;

    // Zugehörige Challenge
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Lesechallenge lesechallenge;

    // Erhaltene Punkte
    @Column(nullable = false)
    private Integer punkte;

    // Name des erhaltenen Abzeichens
    @Column(length = 100)
    private String abzeichenName;

    // Datum der Belohnung
    @Column(nullable = false)
    private LocalDate vergebenAm;

    // Konstruktor für JPA
    protected ChallengeBelohnung() {
    }

    // Konstruktor für neue Belohnungen
    public ChallengeBelohnung(
            Long benutzerId,
            Lesechallenge lesechallenge,
            Integer punkte,
            String abzeichenName,
            LocalDate vergebenAm) {

        this.benutzerId = benutzerId;
        this.lesechallenge = lesechallenge;
        this.punkte = punkte;
        this.abzeichenName = abzeichenName;
        this.vergebenAm = vergebenAm;
    }

    public Long getId() {
        return id;
    }

    public Long getBenutzerId() {
        return benutzerId;
    }

    public Lesechallenge getLesechallenge() {
        return lesechallenge;
    }

    public Integer getPunkte() {
        return punkte;
    }

    public String getAbzeichenName() {
        return abzeichenName;
    }

    public LocalDate getVergebenAm() {
        return vergebenAm;
    }
}
