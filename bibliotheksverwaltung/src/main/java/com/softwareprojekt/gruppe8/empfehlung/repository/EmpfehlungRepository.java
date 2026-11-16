package com.softwareprojekt.gruppe8.empfehlung.repository;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlung;
import org.springframework.data.repository.ListCrudRepository;

/**
 * Speichern und Laden der Empfehlungen
 * @author Niklas Prell
 */

public interface EmpfehlungRepository extends ListCrudRepository<Empfehlung, Long> {

}
