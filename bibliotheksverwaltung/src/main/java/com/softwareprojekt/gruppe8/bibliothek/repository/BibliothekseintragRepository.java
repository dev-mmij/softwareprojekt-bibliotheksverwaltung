package com.softwareprojekt.gruppe8.bibliothek.repository;

import com.softwareprojekt.gruppe8.bibliothek.entity.Bibliothekseintrag;
import com.softwareprojekt.gruppe8.bibliothek.entity.Lesestatus;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BibliothekseintragRepository extends JpaRepository<Bibliothekseintrag, Long> {

    List<Bibliothekseintrag> findByBenutzer(User benutzer);

    List<Bibliothekseintrag> findByBenutzerAndLesestatusOrderByReadlistPositionAsc(User benutzer, Lesestatus lesestatus);

    Optional<Bibliothekseintrag> findByBenutzerAndBuch(User benutzer, Buch buch);

    boolean existsByBenutzerAndBuch(User benutzer, Buch buch);
}