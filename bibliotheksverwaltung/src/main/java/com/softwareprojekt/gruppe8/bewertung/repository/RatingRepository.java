package com.softwareprojekt.gruppe8.bewertung.repository;

import com.softwareprojekt.gruppe8.bewertung.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}