package com.softwareprojekt.gruppe8.konto.repository;

import com.softwareprojekt.gruppe8.konto.entity.RoleType;
import com.softwareprojekt.gruppe8.konto.entity.Rolle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolleRepository extends JpaRepository<Rolle, Long> {

    Optional<Rolle> findByName(RoleType name);
}