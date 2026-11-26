package com.softwareprojekt.gruppe8.empfehlung.repository;

import com.softwareprojekt.gruppe8.empfehlung.domain.Empfehlung;
import com.softwareprojekt.gruppe8.konto.entity.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Speichern und Laden der Empfehlungen
 * @author Niklas Prell
 */

public interface EmpfehlungRepository extends ListCrudRepository<Empfehlung, Long> {

    List<Empfehlung> findAllByBenutzer(User benutzer);

    Optional<Empfehlung> findTopByBenutzerOrderByPositionDesc(User benutzer);

}
