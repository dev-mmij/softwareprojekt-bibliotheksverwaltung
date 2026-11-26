package com.softwareprojekt.gruppe8.tagebuch.repository;

import com.softwareprojekt.gruppe8.tagebuch.domain.Tagebucheintrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Repository für die Verwaltung der Lesetagebucheinträge.
@Repository
public interface TagebucheintragRepository
        extends JpaRepository<Tagebucheintrag, Long> {

    // Findet alle Tagebucheinträge eines Benutzers.
// Sortiert nach Datum: neueste Einträge zuerst.
    List<Tagebucheintrag> findByBenutzerIdOrderByDatumDesc(
            Long benutzerId
    );

    // Findet alle Tagebucheinträge zu einem bestimmten Buch.
    List<Tagebucheintrag> findByBuch_Id(Long buchId);

    // Findet alle Einträge eines Benutzers in einem Zeitraum.
    List<Tagebucheintrag> findByBenutzerIdAndDatumBetween(
            Long benutzerId,
            LocalDate startdatum,
            LocalDate enddatum
    );
}
