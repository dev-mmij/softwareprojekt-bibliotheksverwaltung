package com.softwareprojekt.gruppe8.tagebuch.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "challenge_teilnahme")
public class ChallengeTeilnahme {

    // Eindeutige ID der Teilnahme
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID des Benutzers, der an der Challenge teilnimmt
    @Column(name = "benutzer_id", nullable = false)
    private Long benutzerId;

    // Verbindung zur Lesechallenge
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Lesechallenge lesechallenge;

    // Datum, an dem der Benutzer beigetreten ist
    @Column(nullable = false)
    private LocalDate beitrittsdatum;

    // Aktueller Status der Teilnahme
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TeilnahmeStatus status;

    // Leerer Konstruktor für JPA
    protected ChallengeTeilnahme() {
    }

    // Konstruktor für eine neue Teilnahme
    public ChallengeTeilnahme(Long benutzerId,
                              Lesechallenge lesechallenge,
                              LocalDate beitrittsdatum,
                              TeilnahmeStatus status) {
        this.benutzerId = benutzerId;
        this.lesechallenge = lesechallenge;
        this.beitrittsdatum = beitrittsdatum;
        this.status = status;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public Long getBenutzerId() {
        return benutzerId;
    }

    public Lesechallenge getLesechallenge() {
        return lesechallenge;
    }

    public LocalDate getBeitrittsdatum() {
        return beitrittsdatum;
    }

    public TeilnahmeStatus getStatus() {
        return status;
    }

    // Setter
    public void setBenutzerId(Long benutzerId) {
        this.benutzerId = benutzerId;
    }

    public void setLesechallenge(Lesechallenge lesechallenge) {
        this.lesechallenge = lesechallenge;
    }

    public void setBeitrittsdatum(LocalDate beitrittsdatum) {
        this.beitrittsdatum = beitrittsdatum;
    }

    public void setStatus(TeilnahmeStatus status) {
        this.status = status;
    }
}
