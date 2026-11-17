package com.softwareprojekt.gruppe8.bibliothek.repository;

import com.softwareprojekt.gruppe8.bibliothek.entity.Regal;
import com.softwareprojekt.gruppe8.konto.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegalRepository extends JpaRepository<Regal, Long> {

    List<Regal> findByBenutzerOrderByNameAsc(User benutzer);
}