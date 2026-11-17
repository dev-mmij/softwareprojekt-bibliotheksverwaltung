package com.softwareprojekt.gruppe8.bibliothek.repository;

import com.softwareprojekt.gruppe8.bibliothek.entity.Lesestatus;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BibliothekseintragRepository extends JpaRepository<BibliothekseintragRepository, Long> {

    // alle Buecher eines Benutzers
    List<BibliothekseintragRepository> findByBenutzer(User benutzer);

    // z. B. die Readlist: Lesestatus MOECHTE_ICH_LESEN, sortiert nach Position
    List<BibliothekseintragRepository> findByBenutzerAndLesestatusOrderByReadlistPositionAsc(User benutzer, Lesestatus lesestatus);

    // den Eintrag zu einem bestimmten Buch finden
    Optional<BibliothekseintragRepository> findByBenutzerAndBuch(User benutzer, Buch buch);

    // pruefen, ob ein Buch schon in der Bibliothek ist
    boolean existsByBenutzerAndBuch(User benutzer, Buch buch);
}