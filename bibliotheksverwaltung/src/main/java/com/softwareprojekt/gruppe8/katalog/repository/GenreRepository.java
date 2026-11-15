package com.softwareprojekt.gruppe8.katalog.repository;

import com.softwareprojekt.gruppe8.katalog.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    
    Optional<Genre> findByBezeichnungIgnoreCase(String bezeichnung);
    
    boolean existsByBezeichnungIgnoreCase(String bezeichnung);
}
