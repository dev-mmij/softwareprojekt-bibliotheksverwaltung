package com.softwareprojekt.gruppe8.tagebuch.repository;

import com.softwareprojekt.gruppe8.tagebuch.domain.Lesechallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository für die Verwaltung von Lesechallenges.
// Ermöglicht den Zugriff auf die PostgreSQL-Datenbank.

@Repository
public interface LesechallengeRepository
        extends JpaRepository<Lesechallenge, Long> {

// save(challenge)
// Speichert eine neue Challenge oder aktualisiert eine bestehende.

// findById(id)
// Sucht eine Challenge anhand ihrer ID.

// findAll()
// Gibt alle gespeicherten Challenges zurück.

// deleteById(id)
// Löscht eine Challenge anhand ihrer ID.

// count()
// Zählt alle gespeicherten Challenges.
}
