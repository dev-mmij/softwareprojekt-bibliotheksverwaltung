package com.softwareprojekt.gruppe8.bibliothek.repository;

import com.softwareprojekt.gruppe8.bibliothek.entity.Regal;
import com.softwareprojekt.gruppe8.konto.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegalRepository extends JpaRepository<Regal, Long> {

    // Regal-Uebersicht: Eintraege und Buecher gleich mitladen (fuer Anzahl und Cover)
    @EntityGraph(attributePaths = {"eintraege", "eintraege.buch"})
    List<Regal> findByBenutzerOrderByNameAsc(User benutzer);

    // Regal-Detailansicht: zusaetzlich die Autoren
    @EntityGraph(attributePaths = {"eintraege", "eintraege.buch", "eintraege.buch.autoren"})
    Optional<Regal> findWithEintraegeById(Long id);

    boolean existsByBenutzerAndNameIgnoreCase(User benutzer, String name);
}