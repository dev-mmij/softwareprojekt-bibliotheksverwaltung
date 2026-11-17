package com.softwareprojekt.gruppe8.bewertung.entity;

import com.softwareprojekt.gruppe8.data.AbstractEntity;
import com.softwareprojekt.gruppe8.konto.User;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "rating",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_rating_user_buch",
                columnNames = {"user_id", "buch_id"}
        )
)
public class Rating extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buch_id", nullable = false)
    private Buch buch;

    @Column(nullable = false)
    private int stars;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Rating() {
    }

    @PrePersist
    private void onCreate() {
        validateStars(stars);
        createdAt = Instant.now();
    }

    @PreUpdate
    private void onUpdate() {
        validateStars(stars);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Buch getBuch() {
        return buch;
    }

    public void setBuch(Buch buch) {
        this.buch = buch;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        validateStars(stars);
        this.stars = stars;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    private void validateStars(int stars) {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Sterne muessen zwischen 1 und 5 liegen.");
        }
    }
}