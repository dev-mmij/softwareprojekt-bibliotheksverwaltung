package com.softwareprojekt.gruppe8.empfehlung.repository;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlungsparameter;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

/**
 * Speichern und Laden der Empfehlungsparameter
 * @author Niklas Prell
 */

public interface EmpfehlungsparameterRepository extends ListCrudRepository<Empfehlungsparameter, Long> {
    Optional<Empfehlungsparameter> findByKennung(String kennung);
}
