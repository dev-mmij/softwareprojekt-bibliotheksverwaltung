package com.softwareprojekt.gruppe8.katalog.repository;

import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BuchRepository extends JpaRepository<Buch, Long> {

    Optional<Buch> findByIsbn(String isbn);

    List<Buch> findByTitelContainingIgnoreCase(String titelFragment);

    // Lädt Buch inklusive Verlag, Autoren und Genres direkt mit (verhindert LazyInitializationException in Vaadin)
    @EntityGraph(attributePaths = {"verlag", "autoren", "genres"})
    List<Buch> findAll();

    @EntityGraph(attributePaths = {"verlag", "autoren", "genres"})
    Optional<Buch> findWithDetailsById(Long id);
}
