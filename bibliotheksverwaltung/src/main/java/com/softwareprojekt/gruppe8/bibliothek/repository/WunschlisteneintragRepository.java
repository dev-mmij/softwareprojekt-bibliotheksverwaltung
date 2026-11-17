package com.softwareprojekt.gruppe8.bibliothek.repository;

import com.softwareprojekt.gruppe8.bibliothek.entity.Wunschlisteneintrag;
import com.softwareprojekt.gruppe8.bibliothek.entity.WunschlisteneintragId;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import com.softwareprojekt.gruppe8.konto.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WunschlisteneintragRepository extends JpaRepository<Wunschlisteneintrag, WunschlisteneintragId> {

    List<Wunschlisteneintrag> findByBenutzerOrderByHinzugefuegtAmDesc(User benutzer);

    boolean existsByBenutzerAndBuch(User benutzer, Buch buch);
}