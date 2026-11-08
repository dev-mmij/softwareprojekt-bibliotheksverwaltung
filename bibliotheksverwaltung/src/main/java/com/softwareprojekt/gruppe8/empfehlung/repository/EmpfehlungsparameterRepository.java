package com.softwareprojekt.gruppe8.empfehlung.repository;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlungsparameter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Speichern und Laden der Empfehlungsparameter
 * @author Niklas Prell
 */

@Repository
public interface EmpfehlungsparameterRepository extends CrudRepository<Empfehlungsparameter, Long> {
    Optional<Empfehlungsparameter> findByParameterId(long parameterId);
    Optional<Empfehlungsparameter> findByKennung(String kennung);
}
