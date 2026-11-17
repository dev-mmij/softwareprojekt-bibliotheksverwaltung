package com.softwareprojekt.gruppe8.bibliothek.repository;

import com.softwareprojekt.gruppe8.konto.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegalRepository extends JpaRepository<RegalRepository, Long> {

    List<RegalRepository> findByBenutzerOrderByNameAsc(User benutzer);
}