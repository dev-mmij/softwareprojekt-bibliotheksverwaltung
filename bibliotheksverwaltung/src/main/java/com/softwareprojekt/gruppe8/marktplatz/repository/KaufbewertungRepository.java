package com.softwareprojekt.gruppe8.marktplatz.repository;

import com.softwareprojekt.gruppe8.marktplatz.entity.Kauf;
import com.softwareprojekt.gruppe8.marktplatz.entity.Kaufbewertung;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KaufbewertungRepository extends JpaRepository<Kaufbewertung, Long> {
    List<Kaufbewertung> findByKauf(Kauf kauf);
}
