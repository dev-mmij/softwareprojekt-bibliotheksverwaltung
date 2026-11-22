package com.softwareprojekt.gruppe8.bibliothek.repository;

import com.softwareprojekt.gruppe8.bibliothek.entity.Wunschlisteneintrag;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WunschlisteneintragRepository extends JpaRepository<Wunschlisteneintrag, Long> {

    List<Wunschlisteneintrag> findByBenutzerOrderByHinzugefuegtAmDesc(User benutzer);

    boolean existsByBenutzerAndBuch(User benutzer, Buch buch);
}