package com.softwareprojekt.gruppe8.katalog.repository;

import com.softwareprojekt.gruppe8.katalog.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    
    Optional<Autor> findByNameIgnoreCase(String name);
    
    // Nützlich für Suchfelder in Vaadin (findet z. B. "King" in "Stephen King")
    List<Autor> findByNameContainingIgnoreCase(String nameFragment);
}
