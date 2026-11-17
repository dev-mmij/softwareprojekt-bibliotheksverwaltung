package com.softwareprojekt.gruppe8.marktplatz.repository;

import com.softwareprojekt.gruppe8.data.User;
import com.softwareprojekt.gruppe8.marktplatz.entity.Angebot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AngebotRepository extends JpaRepository<Angebot, Long> {

    List<Angebot> findByStatus(Angebot.Status status);  

    List<Angebot> findByVerkaeufer(User verkaeufer);
    
}
