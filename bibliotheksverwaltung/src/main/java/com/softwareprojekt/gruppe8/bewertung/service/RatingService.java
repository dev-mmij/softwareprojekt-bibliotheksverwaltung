package com.softwareprojekt.gruppe8.bewertung.service;

import java.util.Optional;
import com.softwareprojekt.gruppe8.bewertung.entity.Rating;
import com.softwareprojekt.gruppe8.bewertung.repository.RatingRepository;
import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    // User und Buch muessen in der Datenbank gespeichert sein
    @Transactional
    public Rating saveRating(User user, Buch buch, int stars) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Nutzer unbekannt");
        }
        if (buch == null || buch.getId() == null) {
            throw new IllegalArgumentException("Buch unbekannt");
        }

        Optional<Rating> existingRating = ratingRepository.findRating(user.getId(), buch.getId());

        Rating rating;
        if (existingRating.isPresent()) {
            rating = existingRating.get();
        } else {
            rating = new Rating();
            rating.setUser(user);
            rating.setBuch(buch);
        }

        rating.setStars(stars);
        return ratingRepository.save(rating);
    }
}
