package com.softwareprojekt.gruppe8.tagebuch.repository;

import com.softwareprojekt.gruppe8.tagebuch.domain.ChallengeBelohnung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Datenbankzugriff für erhaltene Challenge-Belohnungen
public interface ChallengeBelohnungRepository
        extends JpaRepository<ChallengeBelohnung, Long> {

    // Alle Belohnungen eines Benutzers anzeigen
    List<ChallengeBelohnung> findByBenutzerId(Long benutzerId);

    // Prüfen, ob ein Benutzer die Belohnung
// für eine Challenge bereits erhalten hat
    boolean existsByBenutzerIdAndLesechallenge_Id(
            Long benutzerId,
            Long challengeId
    );

    // Belohnungen einer bestimmten Challenge anzeigen
    List<ChallengeBelohnung> findByLesechallenge_Id(
            Long challengeId
    );
}
