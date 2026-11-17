package com.softwareprojekt.gruppe8.marktplatz.repository;

import com.softwareprojekt.gruppe8.data.User;
import com.softwareprojekt.gruppe8.marktplatz.entity.Kauf;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



public interface KaufRepository extends JpaRepository<Kauf, Long> {
    List<Kauf> findByKaeuferId(User kaeuferId);
}
