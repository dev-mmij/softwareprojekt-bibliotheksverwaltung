package com.softwareprojekt.gruppe8.empfehlung.repository;

import com.softwareprojekt.gruppe8.empfehlung.domain.Lieblingsgenre;
import org.springframework.data.repository.ListCrudRepository;

/**
 * Speichern und Laden der Lieblingsgenres
 * @author Niklas Prell
 */

public interface LieblingsgenreRepository extends ListCrudRepository<Lieblingsgenre, Long> {

}
