package com.softwareprojekt.gruppe8.bewertung.entity;

import com.softwareprojekt.gruppe8.data.AbstractEntity;
import com.softwareprojekt.gruppe8.konto.entity.User;
import com.softwareprojekt.gruppe8.katalog.entity.Buch;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "buchkommentar")
public class Comment extends AbstractEntity {

    public enum Status {
        VISIBLE,
        HIDDEN
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buch_id", nullable = false)
    private Buch buch;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.VISIBLE;

    @Column(name = "erstellt_am", nullable = false, updatable = false)
    private Instant createdAt;

    public Comment() {
    }

    @PrePersist
    private void onCreate() {
        validateText(text);
        createdAt = Instant.now();
    }

    @PreUpdate
    private void onUpdate() {
        validateText(text);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        validateText(text);
        this.text = text;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Der Status darf nicht fehlen.");
        }
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    private void validateText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Der Kommentar darf nicht leer sein.");
        }
    }
}