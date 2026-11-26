package com.softwareprojekt.gruppe8.empfehlung.repository;

import com.softwareprojekt.gruppe8.empfehlung.domain.Lieblingsgenre;
import com.softwareprojekt.gruppe8.konto.entity.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

/**
 * Speichern und Laden der Lieblingsgenres
 * @author Niklas Prell
 */

public interface LieblingsgenreRepository extends ListCrudRepository<Lieblingsgenre, Long> {
    List<Lieblingsgenre> findByBenutzer(User benutzer);
}
