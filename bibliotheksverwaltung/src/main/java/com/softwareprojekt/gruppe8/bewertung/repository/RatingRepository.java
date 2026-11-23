package com.softwareprojekt.gruppe8.bewertung.repository;

import com.softwareprojekt.gruppe8.bewertung.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query(
            value = "SELECT * FROM bewertung WHERE benutzer_id = :userId AND buch_id = :buchId",
            nativeQuery = true
    )
    Optional<Rating> findRating(@Param("userId") Long userId, @Param("buchId") Long buchId);
}