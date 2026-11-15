package com.softwareprojekt.gruppe8.katalog.repository;

import com.softwareprojekt.gruppe8.katalog.entity.Verlag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerlagRepository extends JpaRepository<Verlag, Long> {
    
    Optional<Verlag> findByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCase(String name);
}
