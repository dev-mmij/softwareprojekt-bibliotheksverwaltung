package com.softwareprojekt.gruppe8.tagebuch.repository;

import com.softwareprojekt.gruppe8.tagebuch.domain.ChallengeTeilnahme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository für die Teilnahme an Lesechallenges.
// Verwaltet die Verbindung zwischen Benutzern und Challenges.

@Repository
public interface ChallengeTeilnahmeRepository
        extends JpaRepository<ChallengeTeilnahme, Long> {

    // Findet alle Teilnahmen eines bestimmten Benutzers.
    List<ChallengeTeilnahme> findByBenutzerId(Long benutzerId);

    // Findet alle Teilnahmen an einer bestimmten Challenge.
    List<ChallengeTeilnahme> findByLesechallenge_Id(Long challengeId);

    // Findet die Teilnahme eines Benutzers an einer Challenge.
    Optional<ChallengeTeilnahme> findByBenutzerIdAndLesechallenge_Id(
            Long benutzerId,
            Long challengeId
    );

    // Prüft, ob ein Benutzer bereits an einer Challenge teilnimmt.
    boolean existsByBenutzerIdAndLesechallenge_Id(
            Long benutzerId,
            Long challengeId
    );
}